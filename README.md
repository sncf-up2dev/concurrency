# Programmation concurrente

La programmation concurrente est un paradigme de programmation qui permet d'exécuter plusieurs tâches en même temps.
Les tâches sont exécutées en parallel dans des fils d'exécution séparés. 
Cela permet d'optimiser l'utilisation des ressources du processeur et d'améliorer la réactivité des programmes.

Malgré ses avantages, la programmation concurrente a des conséquences importantes sur l'exécution des programmes et la bonne santé des applications.
Cela est davantage vrai lorsque certaines resources sont simultanément manipulées par plusieurs fils d'exécution.
Il convient donc de faire une attention particulière à l'intégrité des données dans ces cas-là.

## Java et la programmation concurrente

Java supporte la programmation concurrente. Un fils d'exécution est représenté par un objet de la classe `Thread`.
Un programme Java peut créer plusieurs fils d'exécution et les exécuter en même temps.

Il existe plusieurs manières de créer un fils d'exécution en Java.
Par exemple, on pourrait créer une instance de la classe `Thread` via son constructeur.
Le constructeur prend en paramètre un objet de type `Runnable` qui représente le code à exécuter dans le fils d'exécution.
Le code sera exécuté d'une manière asynchrone lorsque la méthode `start` est appelée sur l'objet de type `Thread`.

### Code à fil sécurisé
Un code à fil sécurisé (aussi appelé thread-safe) est un code qui se comporte de la manière attendue quand il est exécuté par plusieurs fils d'exécution en même temps.
Dans la plupart des cas, Java n'assure pas la sécurité des fils d'exécution. C'est au développeur donc de réaliser des codes à fil sécurisé.

La classe `IntegerWrapperTest` a pour objectif de vérifier le bon comportement de la classe `IntegerWrapper` : une simple classe encapsulant un entier.

La classe de test contient un exemple de la création d'un fils d'exécution via le constructeur dans le test `incrementMultiThread`.
Ce test lance deux fils d'exécution en même temps. Chaque fil augmente la valeur stockée dans `wrapper`, un objet de type `IntegerWrapper`, N fois.
À la fin d'exécution de deux fils d'exécution (attente garantie grâce aux appels de la méthode `join()`), la valeur stockée dans `wrapper` devrait être égale à `2 * N`.
Le test néanmoins échoue, raison pour laquelle la classe `IntegerWrapper` n'est pas à fil sécurisé telle qu'elle est dans le code fourni.

La classe `ListUtuls` démontre une autre manière de créer des fils d'exécution en Java.
En appelant la méthode `parallel()` sur un stream d'objets, l'exécution des opérations sur le stream sera effectué en parallèle, c'est-à-dire sur plusieurs fils d'exécution.
C'est ce qui fait la méthode `copyUnordered` dans l'objectif de paralléliser le processus de replication d'une liste sans prendre en compte l'ordre des éléments.

La classe `ListUtilsTest` contient un test qui vérifie le bon comportement de la méthode `copyUnordered`.
Encore une fois le test échoue, car la méthode `copyUnordered` n'est pas à fil sécurisé. [^1]

[^1]: Le problème ici est encore pire qu'un test qui échoue.
Si vous lancez le test `copyUnordered` un nombre suffisant des fois, vous pourriez avoir une erreur d'exécution `ArrayIndexOutOfBoundsException`.
Cette exception est inexplicable dans un contexte de programmation séquentielle, et est dûe à la nature non sécurisée vis-à-vis aux fils d'exécution de la classe `ArrayList`.

### Atomicité des opérations
Une opération est dite atomique si elle est exécutée en une seule fois, sans être interrompue par un autre fil d'exécution.
En Java, beaucoup d'opérations ne sont pas atomiques, même si elles s'écrivent avec une seule instruction.

Par example, l'instruction `i++` est équivalente à `i = i + 1` qui se constitue de trois opérations élémentaires :
* lecture de la valeur de `i`,
* incrémentation de la valeur,
* écriture de la nouvelle valeur dans `i`.

C'est pour cette raison que l'opération `i++` n'est pas atomique.

La nature non atomique des opérations est une source des comportements non conforme lorsque le code est exécuté en fils multiples d'exécution.
Imaginons que deux fils d'exécution réalisent l'opération `i++` simultanément. Étant donné une valeur initiale de zéro, on attend que la valeur de `i` soit 2 à la fin.
Mais si le premier fil lit la valeur `i` puis il donne la main au deuxième fil avant de l'écriture de la valeur augmentée, les deux fils lisent une valeur de zéro et écrivent une valeur de 1.
On obtient alors 1 comme résultat final.

Heureusement, Java fournit des classes qui encapsulent des opérations atomiques pour permettre la manipulation de données partagées par plusieurs fils d'exécution en toute sécurité.
Par exemple, les classes `AtomicInteger`, `AtomicLong` et `AtomicBoolean` fournissent des opérations atomiques sur des entiers des longs et des booléens respectivement.

### Zone critique et synchronisation
Une zone critique est une partie du code qui manipule, en lecture et en écriture, des données partagées par plusieurs fils d'exécution.
Une telle zone ne doit pas être exécutée par plusieurs fils d'exécution en même temps, car cela peut conduire à des résultats inattendus et à des erreurs d'exécution.

Java fournit des mécanismes de synchronisation pour protéger les zones critiques.

Le mot clef `synchronized` permet de définir une partie du code qui ne peut être exécutée que par un seul fil d'exécution à la fois pour un objet donné.
Cet objet représente le verrou de synchronisation et est passé entre parenthèses après le mot clef `synchronized`.
Le bloc de code qui suit représente une zone critique.
Les autres fils d'exécution qui tentent d'exécuter la zone critique en utilisant le même verrou sont mis en attente jusqu'à ce que le premier fil d'exécution ait terminé l'exécution de cette zone.

```java
Object verrou = new Object();

synchronized (verrou) {
    // Zone critique
}
```

Le mot clef `synchronized` peut aussi être utilisé sur une méthode entière.
Pour les méthodes d'instance, le verrou de synchronisation sera l'objet sur lequel la méthode est appelée.
Pour les méthodes de classe (méthodes statiques), le verrou de synchronisation sera la classe de la méthode.

Les codes suivants sont donc équivalents :

```java

class Foo {
    public synchronized void bar() {
        // Zone critique 1
    }
    
    public static synchronized void baz() {
        // Zone critique 2
    }
}
```

```java

class Foo {
    public void bar() {
        synchronized (this) {
            // Zone critique 1
        }
    }
    
    public static void baz() {
        synchronized (Foo.class) {
            // Zone critique 2
        }
    }
}
```

## Objectifs

### Classe `IntegerWrapper`

Utilisez les divers outils fournis par Java pour répondre aux problématiques de concurrence pour faire en sorte que la classe `IntegerWrapper` soit à fil sécurisé.
Votre solution doit faire passer tous les tests fournis dans la classe `IntegerWrapperTest` sans les modifier.

Y a-t-il une autre solution que vous pourriez proposer pour réaliser le même objectif ?

### Classe `ListUtils`

La liste `copy` créée par la méthode `copyUnordered` n'est pas à fil sécurisé. C'est pour cette raison que le test `copyUnordered` échoue et qu'on court le risque d'obtenir une erreur d'exécution `ArrayIndexOutOfBoundsException`.

Java fourni un moyen de créer une liste à fil sécurisé en Java. Utilisez ce mécanisme pour faire passer le test dans `ListUtilsTest` sans le modifier.


