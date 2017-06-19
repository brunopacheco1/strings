# Strings

RESTFul project for String Set operations, developed in Java EE 7.

Environment:
- Google Compute Engine: g1-small (1 vCPUj, 1.7 GB)
- SO: Ubuntu 14.04, AMI Bitnami Wildfly 11.x
- Java EE 7
- Address: http://apps.bruno-pacheco.eng.br/strings
- Swagger: http://apps.bruno-pacheco.eng.br/strings/api/swagger.yaml


Comments about the project:

For JMeter load test, please check test.jmx. It is simple, just a loop of requests to upload sets of strings.

To deploy, nothing especial is necessary, just deploy the project in a Wildfly.

I really enjoyed this challange! I've been working with Java 8 for a short time, lesser with Lambda or functional programming. I have some experience on Scala, during Big Data classes and other personal projects. On this project, I've tryed to use most of the time lambda to practice.

About difficulties, the most difficult one was the service that returns the longest chain. Actually, I didn't figure out the correct meaning of the problem. I've started the development thinking that I was supposed to return the longest chain of all strings uploaded. After reading again the assingment, I've realized the I was wrong, but even so I didn't get the idea of the jumping part. That's the reason you will find in my code both versions of what I've understood.

About computational and space complexity, I'll explain the first idea, the longest chain of all strings uploaded:

To preper the chain index, the computational complexity is O(W\*N), which W is the number of strings in a Set of Strings and N is the number of elements in the Word Index. The Space complexity of this index is O(N\*N!) because the worst result is a tree of N elements, each one combined with N-1 diferent elements.

To search the longest chain in a graph, or its diameter, I've faced some problems. To simplify the project, the software generates all path options and, after that, it choose the longest one according to the number of its vertices. I know that it is the worst option, but I didn't found algorithms on Internet that could help me on that. Actually, I've made some researches about directed graphs and I've found the Directed Acyclic Graph, but I need time to understand it.

ERRATA: I've made some mistakes about space complexity. I've put O(N\*(N!/(N-1)!), but the correct is O(N\*(N!/(N-N)!), or simply  O(N\*N!). For each N element, the worst case is the combination of all N elements, without repetion and the order of the elements matters.