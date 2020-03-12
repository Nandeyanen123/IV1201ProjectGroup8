#!/bin/bash
#Förutsätter att databasen test3 är skapad lokalt
USER=grupp8
PASS=Iv1201...
DB=test3

mysql -u $USER -p$PASS $DB < herokudump.sql