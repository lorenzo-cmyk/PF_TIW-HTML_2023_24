# Progetto per Tecnologie Informatiche per il Web (Versione HTML) - A.A. 2023/2024

- Scadenza: 2024-09-01
- Voto: 30/30

# Descrizione

L'applicazione è un sistema di gestione documentale (DMS) che consente agli utenti autenticati di organizzare e gestire i propri documenti all'interno di una struttura gerarchica di cartelle. Ogni utente ha accesso a una Home Page che visualizza l'albero delle proprie cartelle. Da qui è possibile navigare tra i contenuti, creare nuove cartelle e documenti, visualizzare i dettagli dei documenti e spostare i file tra le diverse directory. L'applicazione fornisce strumenti essenziali per la gestione dei documenti e delle relative cartelle.

# Compilazione

Il progetto è gestito tramite Maven; per compilare il file WAR è sufficiente eseguire:

```bash
mvn clean package
```

Per eseguire l'intero stack applicativo è possibile sfruttare Docker:

```bash
docker-compose up
```

Per compilare in formato PDF la documentazione:

```bash
cd docs
pdflatex -shell-escape main.tex
```
