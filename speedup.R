library(ggplot2)

file_path <- "result_pi.txt"
data <- readLines(file_path)

speedupValues <- c(1)
nWorkersValues <- c(1)
for(line in data){
    if(grepl("Speedup:", line)){
        parts <- unlist(strsplit(line, ","))
        speedup <- as.numeric(gsub("Speedup: ", "", parts[1]))
        nWorkers <- as.numeric(gsub("Temps avec ", "", gsub(" worker.*", "", parts[3])))

        if(!is.na(speedup)&& !is.na(nWorkers)){
            speedupValues <- c(speedupValues, speedup)
            nWorkersValues <- c(nWorkersValues, nWorkers)
        }
    }
}

speedupData <- data.frame(nWorkers = nWorkersValues, speedup = speedupValues)

ggplot(speedupData, aes(x= nWorkers, y = speedup)) +
    geom_line() +
    geom_point() +
    labs(title = "Speedup du programme Pi", x = "nombre de processus" , y = "SP") +
    theme_minimal()