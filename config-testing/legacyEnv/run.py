#!/usr/bin/python
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# See the NOTICE file distributed with this work for additional
# information regarding copyright ownership.
#


import re
import os
import sys
import shutil
import StringIO
import subprocess
import ConfigParser
import xml.etree.ElementTree as ET

CONFIG_NAME = 'config.ini'
SCRIPT_ABSOLUTE_PATH = os.path.dirname(os.path.realpath(__file__))
CONFIG_SECTION = 'runConfiguration'
RESOURCE_ARCH_NAME = 'resources.tar'

SETTING_DOKCERFILE_PATH = 'docker_image_dir'
SETTING_IMAGE_NAME = 'docker_image_name'


def get_thingml_pom_version(pom_xml_file):
	print "Retrieving ThingML version from: " + pom_xml_file
	pom_xml = ET.parse(pom_xml_file)
	root = pom_xml.getroot()
	m = re.match('\{.*\}', root.tag)
	ns = m.group(0) if m else ''
	version = root.find(ns + 'version')
	print "ThingML version: " + version.text
	return version.text

def copy_dir_contents(src, dst, symlinks=False, ignore=None):
	for item in os.listdir(src):
		s = os.path.join(src, item)
		d = os.path.join(dst, item)
		if os.path.isdir(s):
 			shutil.copytree(s, d, symlinks, ignore)
		else:
			shutil.copy2(s, d)


def print_job_std(job, stdout, stderr):
	for line in StringIO.StringIO(stdout).readlines():
		print '[STDOUT ' + job + '] ' + line.rstrip()

	for line in StringIO.StringIO(stderr).readlines():
		print '[STDERR ' + job + '] ' + line.rstrip()


def check_and_print_sdt(stdout, stderr):
	for line in StringIO.StringIO(stdout).readlines():
		print line.rstrip()

	if stderr:
		sys.stderr.write(stderr)
		sys.exit(1)


def build_docker_image(dockerfile_path, docker_image_name, sut_folder):
	dockerfile_abs = os.path.join(SCRIPT_ABSOLUTE_PATH, dockerfile_path)

	print "Copying SUT from " + sut_folder
	ignore_config_testing = lambda directory, contents: ['config-testing'] if os.path.isdir(os.path.join(directory, 'config-testing')) and 'config-testing' in contents else []
	shutil.copytree(sut_folder, dockerfile_abs + "/bvr-diversity", symlinks=False, ignore=ignore_config_testing)
	
	command = ['docker', 'build', '--rm', '-t', docker_image_name, '.']
	print 'Building image: ' + ' '.join(command) + ' in ' + dockerfile_abs
	proc = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, cwd=dockerfile_abs)
	stdout, stderr = proc.communicate()
	check_and_print_sdt(stdout, stderr)



def get_master_ip_address():
	command = ['hostname', '-i']
	print "Find out master IP: " + ' '.join(command)
	proc = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
	stdout, stderr = proc.communicate()
	sys.stdout.write(stdout)
	sys.stderr.write(stderr)
	if not stdout:
		sys.stderr.write("Stdout for '" + " ".join(command) + "' is empty\n")
		return ''

	match = re.search("(?:[0-9]{1,3}\.){3}[0-9]{1,3}", stdout)
	master_ip = '' if not match else match.group(0)
	if not master_ip:
		sys.stderr.write("Cannot find ip address of the master in stdout of '" + " ".join(command) + "': '" + stdout + "'\n")
	return master_ip


def execute_tests(jobs, **slave_params):
	script = slave_params.get('script')
	master_job_root = slave_params.get('master_job_root')
	master_ip = slave_params.get('master_ip')
	master_ssh_port = slave_params.get('master_ssh_port')
	master_user = slave_params.get('master_user')
	master_pass = slave_params.get('master_pass')
	docker_worker_image = slave_params.get('docker_worker_image')
	processes = {}

	#run all testing
	for job in jobs:
		#docker run --rm vassik/thingml-test-worker:v0.1 ./dojob.py /master/ job1 172.17.0.6 22 thmlslave thmlslave
		command = ['docker', 'run', '--rm', docker_worker_image, script, master_job_root,
			job, master_ip, master_ssh_port, master_user, master_pass]
		print 'Executing: ' + ' '.join(command)
		processes[job] = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

	#wait for all jobs to complete
	for job in jobs:
		stdout, stderr = processes.get(job).communicate()
		print_job_std(job, stdout, stderr)

def get_header_footer_path(category_report_folder, jobs, header_footer_name):
	for job in jobs:
		report_job_folder = os.path.join(category_report_folder, job)
		header_path = os.path.join(report_job_folder, header_footer_name)
		if os.path.isfile(header_path):
			return header_path
	return ""


def prepare_report(working_folder, jobs, report_folder, category_name):
	#we expect archive with results in tmp.tar
	header_accumulated_result = ''
	body_accumulated_result = ''
	footer_accumulated_result = ''
	category_report_folder = os.path.join(report_folder, category_name)
	os.mkdir(category_report_folder)
	for job in jobs:
		job_folder = os.path.join(working_folder, job)
		result_job_arch_file = os.path.join(job_folder, "tmp.tar")
		if not os.path.isfile(result_job_arch_file):
			print "Cannot find file with results for the '" + job + "' routine (this job probably failed). Skipping..."
			continue

		command= ['tar', '-xf', 'tmp.tar']
		print "Executing: '" + ' '.join(command) + "' in " + job_folder
		proc = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, cwd=job_folder)
		stderr, stdout = proc.communicate()
		check_and_print_sdt(stdout, stderr)

		#copying content of the job result folder to the folder that accumulates results for the entity category
		result_job_folder = os.path.join(job_folder, 'tmp')
		result_job_report_folder = os.path.join(category_report_folder, job)
		os.mkdir(result_job_report_folder)
		copy_dir_contents(result_job_folder, result_job_report_folder)

		#copy src directory
		#src_result_job_report_folder = os.path.join(result_job_report_folder, 'src')
		#os.mkdir(src_result_job_report_folder)
		#copy_dir_contents(os.path.join(job_folder, 'src'), src_result_job_report_folder)

		job_body = ""
		with open(os.path.join(result_job_folder, 'results.html'), 'r') as file:
			job_body = file.read()

		#modify urls, job name should precede all urls
		pattern = '(?<=href=\").+(?=\")'
		matches = re.findall(pattern, job_body)
		for old_path in matches:
			new_path = os.path.join(job, old_path.lstrip('/'))
			job_body = job_body.replace(old_path, new_path)

		body_accumulated_result = body_accumulated_result + job_body

	#footer and header should be in any of job folder, take the one we find first
	header_path = get_header_footer_path(category_report_folder, jobs, "header.html")
	footer_path = get_header_footer_path(category_report_folder, jobs, "footer.html")
	if header_path:
		with open(header_path, 'r') as file:
			header_accumulated_result = file.read()

	if footer_path:
		with open(footer_path, 'r') as file:
			footer_accumulated_result = file.read()

	#copy js and stuff for report and fix the link
	listjs_name = 'listjs.js'
	report_templ_dir = os.path.join(SCRIPT_ABSOLUTE_PATH, 'report_templates')
	listjs = os.path.join(report_templ_dir, listjs_name)
	shutil.copy2(listjs, os.path.join(category_report_folder, listjs_name))

	match = re.search("(?<=src=\").+(?=\")", footer_accumulated_result)
	if match:
		old_path = match.group(0)
		footer_accumulated_result = footer_accumulated_result.replace(old_path, listjs_name)

	final_results_path = os.path.join(category_report_folder, 'results.html')
	with open(final_results_path, 'w') as file:
		file.write(header_accumulated_result + body_accumulated_result + footer_accumulated_result)
	

def run_routine(category_name, working_folder, report_folder, master_slave_user, master_slave_pwd, master_ssh_port, sut_folder):
	config = ConfigParser.ConfigParser()
	config.read(os.path.join(SCRIPT_ABSOLUTE_PATH, CONFIG_NAME))

	dockerfile_path = config.get(CONFIG_SECTION, SETTING_DOKCERFILE_PATH)
	docker_image_name = config.get(CONFIG_SECTION, SETTING_IMAGE_NAME)



	master_ip = get_master_ip_address()
	if not master_ip:
		sys.exit(1)

	slave_params = {'script' : './dojob.py', 'master_job_root' : working_folder, 
		'master_ip' : master_ip, 'master_ssh_port': master_ssh_port , 'master_user' : master_slave_user,
		'master_pass' : master_slave_pwd, 'docker_worker_image' : docker_image_name}

	jobs = ['job1']
	for job in jobs:
		os.mkdir(os.path.join(working_folder, job))

	build_docker_image(dockerfile_path, docker_image_name, sut_folder)
	execute_tests(jobs, **slave_params)
	prepare_report(working_folder, jobs, report_folder, category_name)


if __name__ == "__main__":
	category_name = sys.argv[1]
	working_folder = sys.argv[2]
	report_folder = sys.argv[3]
	sut_folder = sys.argv[4]

	master_slave_user = os.environ.get('MASTER_SLAVE_USER')
	master_slave_pwd = os.environ.get('MASTER_SLAVE_PWD')
	master_ssh_port = os.environ.get('MASTER_SSH_PORT')

	if not master_slave_user:
		message = "MASTER_SLAVE_USER env variable is not set!. Exiting..."
		sys.stderr.write(message + '\n');
		sys.exit(1)

	if not master_slave_pwd:
		message = "MASTER_SLAVE_PWD env variable is not set!. Exiting..."
		sys.stderr.write(message + '\n');
		sys.exit(1)

	if not master_ssh_port:
		message = "MASTER_SSH_PORT env variable is not set!. Exiting..."
		sys.stderr.write(message + '\n');
		sys.exit(1)

	run_routine(category_name, working_folder, report_folder, master_slave_user, master_slave_pwd, str(master_ssh_port), sut_folder)
	
	sys.exit(0)
