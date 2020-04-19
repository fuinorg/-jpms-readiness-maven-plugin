# jpms-readiness-maven-plugin
Maven plugin that verifies the readiness of all dependencies in regard to the Java Platform Module System (JPMS)

## Run the plugin
Run the plugin in your project's root directory.

Either using [Maven Wrapper](https://github.com/takari/maven-wrapper): 
```
./mvnw org.fuin.jpmsr:jpms-readiness-maven-plugin:0.2.0-SNAPSHOT:verify
```
Or the classic Maven installation: 
```
mvn org.fuin.jpmsr:jpms-readiness-maven-plugin:0.2.0-SNAPSHOT:verify
```

## Example output
The output should be similar to this example:
```diff
 [INFO] =============================================
 [INFO] Java Platform Module System (JPMS) Readiness:
 [INFO] =============================================
 [INFO] jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.2:compile => module-info
![WARNING] jakarta.ejb:jakarta.ejb-api:jar:3.2.6:compile => No module-info and no Automatic-Module-Name
 [INFO] jakarta.json:jakarta.json-api:jar:1.1.6:compile => module-info
 [INFO] jakarta.json.bind:jakarta.json.bind-api:jar:1.0.2:compile => module-info
![WARNING] jakarta.interceptor:jakarta.interceptor-api:jar:1.2.5:compile => No module-info and no Automatic-Module-Name
![WARNING] jakarta.mail:jakarta.mail-api:jar:1.6.4:compile => No module-info and no Automatic-Module-Name
 [INFO] jakarta.activation:jakarta.activation-api:jar:1.2.1:compile => Automatic-Module-Name
 [INFO] jakarta.validation:jakarta.validation-api:jar:2.0.2:compile => Automatic-Module-Name
 [INFO] jakarta.annotation:jakarta.annotation-api:jar:1.3.5:compile => Automatic-Module-Name
![WARNING] jakarta.enterprise:jakarta.enterprise.cdi-api:jar:2.0.2:compile => No module-info and no Automatic-Module-Name
 [INFO] jakarta.transaction:jakarta.transaction-api:jar:1.3.2:compile => Automatic-Module-Name
 [INFO] org.fuin:utils4j:jar:0.11.0-SNAPSHOT:compile => Automatic-Module-Name
 [INFO] jakarta.persistence:jakarta.persistence-api:jar:2.2.3:compile => Automatic-Module-Name
![WARNING] jakarta.inject:jakarta.inject-api:jar:1.0:compile => No module-info and no Automatic-Module-Name
 [INFO] =============================================
```

## Snapshots

Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your .m2/settings.xml (section "repositories") to enable snapshots in your Maven build:
```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
An additional entry to the "pluginRepositories" section is also required:
```xml
<pluginRepository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</pluginRepository>
```
