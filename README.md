# [Kelsey](https://www.wikiwand.com/en/Frances_Oldham_Kelsey) Portal

## Building

Build with maven

```sh
mvn clean package
```

*Note*: spring-data-neo4j 3.2.1-RELEASE is compatible with Neo4j-2.1.5



## Usage

```
usage: Kelsey Portal 
 -c,--cui <arg>     CUI of disease or indicator (symptom or sign or
                    finding)
 -f,--feed <arg>    Feed [file] annotated data into neo4j. After seeding,
                    program exits, without executing any queries.
 -l,--limit <arg>   Maximum number of results. Default 10
 -q,--query <arg>   Run query:
                    1: For a given disease (CUI), what other diseases have
                    the highest number of common indicators?
                    2: For a given disease (CUI), what indicators connect
                    it to other diseases
                    3: For an indicator (CUI), what other indicators
                    commonly present in diseases where said indicator is
                    present?
                    4: Which diseases are most similar to each other
                    (share indicators) (No CUI)?
                    5: List diseases (No CUI)
                    5: List indicators (No CUI)
 -s,--skip <arg>    Results offset. Default 0
```

Seed data with JSON feed

```sh
java -cp "lib/*:target/*" org.dsc.diseametry.App --feed diseasesData/adam_articles.json
```

Run queries

```sh
java -cp "lib/*:target/*" org.dsc.diseametry.App --query 5
```


## Conceptual Model

The goal of this application is to uncover relationships between diseases that share symptoms/signs or findings in patients.

The following questions need to be answerable with the system:

  - Given a disease, what other diseases have the highest number of common indicators (symptom/sign or finding)?
  - Given a disease, what indicators connect it to other diseases?
  - Given an indicator, what other indicators commonly present in diseases where said indicator is present?

### Data used
In the example at hand the A.D.A.M. encyclopedia was used. In order to do so we wrote a crawler to crawl all articles from this encyclopedia.
The crawler can be found [here](https://github.com/ph1l337/adam-crawler "A.D.A.M. Crawler").
From the obtained articles we only considered the ones that have a symptoms section in order to introduce as few a possible noise.

In general this application is intended for any document that contains information on diseases and their related symptoms/signs and/or findings, like research papers, web pages or encyclopedias,.

Since the documents are annotated with UMLS/SNOMED notations, we first to understand some relevant vocabulary for it.

UMLS & SNOMED terminology:

  - CUI (Concept): a meaning, e.g. headache
  - SUI (String ID): a string/form of text, e.g. Headache vs HEADACHE vs headache
  - TUI (Semantic type): categorization of a concept, in a hierchical structure, e.g. biological term, chemical compound


How is it all connected:

  - Different string formats can map to the same meaning, i.e. CUI (concept)
  - One meaning can have different categorizations, i.e. TUI (semantic type), regardless of its expressed format, i.e. SUI (string ID)
  - The actual categorization (semantic type, TUI) of a meaning (concept, CUI) is dependent on context; that's what's attempted with NLP: identifying the correct categorization of a meaning
  - A single unique string, i.e. SUI, can refer to different meanings (concept, CUI)

Therefore, the following is concluded:

  - When reading documents, the actual string format is not relevant. What is relevant is its meaning, i.e CUI
  - Each disease, and each symptom/sign or finding will have its own unique meaning, which will be different from any other concept

Modeling wise, we establish the following entities are required to represent the diseases and symptoms/sign and findings:

  - A disease entity, with a unique CUI
  - A indicator entity with a unique CUI, to represent symptom/sign, and findings
  - A `HAS_FINDING` relationship between a (Disease) entity and an (Indicator) entity
  - A `HAS_SYM_OR_SIGN` relationship between a (Disease) entity and an (Indicator) entity

Note that this model excludes the following information:
  - Reference of the source of the connections between diseases and indicators
  - Count/Stock of how many references present a connection a disease and an indicator
