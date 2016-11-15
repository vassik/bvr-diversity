
library(ggplot2);



data <- data.frame(read.csv(file="results.csv", header = TRUE, strip.white=TRUE));
data$complexity <- with(data, round(1 - (products / (2**features)), 2));

pl_descriptions <- c(
  "PL. 1 (3, 100%)", 
  "PL. 2 (3, 50%)", 
  "PL: 3 (15, 100%)", 
  "PL. 4 (15, 0.01%)"
  );

ggplot(data, aes(y=diversity, x=pl, fill=kind, col=kind)) + 
  labs(x="\nProduct Line", y="Functional Attribute Diversity (FAD)\n") +
  scale_x_discrete(breaks=c("pl_1", "pl_2", "pl_3", "pl_4"), labels=pl_descriptions) +
  scale_fill_discrete(name="Strategy:",
                      breaks=c("ga", "random"),
                      labels=c("Genetic Algorithm", "Random Sampling")) +
  scale_colour_discrete(name="Strategy:",
                      breaks=c("ga", "random"),
                      labels=c("Genetic Algorithm", "Random Sampling")) +
  geom_point(position=position_jitterdodge(), col="grey") +
  geom_boxplot(alpha=0.65) +
  theme_bw() +
  theme(axis.title= element_text(face="bold"),
        legend.key = element_blank(),
        legend.justification=c(1,0), 
        legend.position=c(1,0))

dev.copy(pdf, "diversity.pdf", width=10, height=7);
dev.off();


ggplot(data, aes(y=coverage, x=pl, fill=kind, col=kind)) + 
  labs(x="\nProduct Line", y="Coverage (single feature)\n") +
  scale_x_discrete(breaks=c("pl_1", "pl_2", "pl_3", "pl_4"), labels=pl_descriptions) +
  scale_fill_discrete(name="Strategy:",
                      breaks=c("ga", "random"),
                      labels=c("Genetic Algorithm", "Random Sampling")) +
  scale_colour_discrete(name="Strategy:",
                        breaks=c("ga", "random"),
                        labels=c("Genetic Algorithm", "Random Sampling")) +  geom_point(position=position_jitterdodge(), col="grey") +
  geom_boxplot(alpha=0.65) +
  theme_bw() +
  theme(axis.title= element_text(face="bold"),
        legend.key = element_blank(),
        legend.justification=c(1,0), 
        legend.position=c(1,0))


dev.copy(pdf, "coverage.pdf", width=10, height=7);
dev.off();

# boxplot(
#   diversity ~ kind * pl, 
#   ylab="FAD",
#   ylim=c(0, 1), 
#   col=2:3
# );


