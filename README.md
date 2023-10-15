# Lightweight Java Plugin Framework

# This is a fork

[![Maven Central](https://img.shields.io/maven-central/v/org.javacentric.gjerd/light-jpf.svg)](http://search.maven.org/#search|ga|1|light-jpf)
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)](https://travis-ci.org/souzen/light-jpf)



## 1. Features
- Simple api
- Sandboxing with custom java classloader
- Build plugins with maven



## 2. Usage

#### 2.1 Create plugin

Create Plugin class that implements ljpf.Plugin interface.

```java
public class CustomPlugin implements Plugin {

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }
}
```

Create descriptor file for corresponding plugin and place it in project resources.
Descriptor file must have .plugin extension.

src/main/resources/custom.plugin

```properties
id=CustomPluginId
version=0.0.1
pluginClass=ljpf.examples.plugin.CustomPlugin
description=My Custom plugin
```

#### 2.2 Create app

Load plugins in your main application using PluginManager interface. Use plugin id from descriptor file to load extensions.
Plugin repository determines way of loading plugins. Base case is to load jars from classpath plugins/ directory.


```java
public class App {

    public static void main(String[] args) {

        PluginRepository pluginRepository = new DirPluginRepository("plugins");
        PluginManager pluginManager = new DefaultPluginManager(pluginRepository);

        pluginManager.load("CustomPluginId");
    }

}
```

#### 2.3 Build with Maven

#### Build plugin with Maven

Use light-jpf-maven-plugin to create maven artifact (*-plugin.jar).
Maven will create fat jar with plugin code and all its dependencies. 

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.javacentric.gjerd</groupId>
                <artifactId>light-jpf-maven-plugin</artifactId>
                <version>0.0.2</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>make-plugin</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        ....    
    
    </build>
```

#### Add plugins to app

light-jpf-maven-plugin can also prepare plugins/ directory in your application.

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.javacentric.gjerd</groupId>
                <artifactId>light-jpf-maven-plugin</artifactId>
                <version>0.0.2</version>
                <executions>
                    <execution>
                        <phase>process-sources</phase>
                        <goals>
                            <goal>make-plugin-repository</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/plugins</outputDirectory>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>ljpf.examples.plugin</groupId>
                                    <artifactId>custom-plugin</artifactId>
                                    <version>0.0.1</version>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            ... 
               
        </plugins>
        ....    
    
    </build>
```



## 3. Plugin Repositories

#### DirPluginRepository
TODO Loads plugins from given directory

#### ClasspathPluginRepository
TODO Loads plugins from java classpath

#### MultiPluginRepository
TODO Enables mixing multiple plugin reposiotories



## 4. Debugging
In app create plugins dir and run or place plugins as dependencies

```xml
    <dependencies>
        <dependency>
            <groupId>ljpf.examples.plugin</groupId>
            <artifactId>custom-plugin</artifactId>
            <version>0.0.1</version>
        </dependency>
        ...
        
    </dependencies>
```



## 5. Examples
See example project [here](https://github.com/souzen/light-jpf/tree/master/examples)

Build and run example

```
mvn clean package
./run

Should result in the following output:

09:28:13.087 [main] INFO  App - Initializing...
09:28:13.089 [main] WARN  DirPluginRepository - Dir Plugin Repository not found /home/user/Sources/light-jpf/plugins
09:28:13.090 [main] DEBUG DirPluginRepository - Loading plugins from directory: /home/user/Sources/light-jpf/examples/app/target/plugins
09:28:13.094 [main] DEBUG BasePluginRepository - Plugin added: SecondPlugin /home/user/Sources/light-jpf/examples/app/target/plugins/second-plugin
09:28:13.094 [main] DEBUG BasePluginRepository - Plugin added: FirstPlugin /home/user/Sources/light-jpf/examples/app/target/plugins/first-plugin
09:28:13.094 [main] DEBUG BasePluginRepository - Plugin added: ThirdPlugin /home/user/Sources/light-jpf/examples/app/target/plugins/third-plugin
09:28:13.098 [main] DEBUG FirstPlugin - Load [classloader ljpf.loader.ParentLastClassLoaderFactory$ParentLastClassLoader@18eed359]
09:28:13.098 [main] INFO  FirstPlugin - Spring version 5.2.19.RELEASE
09:28:13.098 [main] DEBUG DefaultPluginManager - Plugin Loaded: Plugin{id='FirstPlugin', version=0.0.8-SNAPSHOT}
09:28:13.100 [main] DEBUG SecondPlugin - Load [classloader ljpf.loader.ParentLastClassLoaderFactory$ParentLastClassLoader@23e028a9]
09:28:13.100 [main] INFO  SecondPlugin - Spring version 5.2.19.RELEASE
09:28:13.101 [main] DEBUG DefaultPluginManager - Plugin Loaded: Plugin{id='SecondPlugin', version=0.0.8-SNAPSHOT}
09:28:13.101 [main] DEBUG ThirdPlugin - Load [classloader ljpf.loader.ParentLastClassLoaderFactory$ParentLastClassLoader@50b494a6]
09:28:13.101 [main] INFO  ThirdPlugin - Hello World!
09:28:13.101 [main] INFO  ThirdPlugin - Hello From Plugin Resource!
09:28:13.101 [main] DEBUG DefaultPluginManager - Plugin Loaded: Plugin{id='ThirdPlugin', version=0.0.8-SNAPSHOT}
09:28:13.101 [main] INFO  App - Shutting down...
09:28:13.102 [main] INFO  FirstPlugin - Unload
09:28:13.102 [main] DEBUG DefaultPluginManager - Plugin Unloaded: Plugin{id='FirstPlugin', version=0.0.8-SNAPSHOT}
09:28:13.102 [main] INFO  SecondPlugin - Unload
09:28:13.102 [main] DEBUG DefaultPluginManager - Plugin Unloaded: Plugin{id='SecondPlugin', version=0.0.8-SNAPSHOT}
09:28:13.102 [main] INFO  ThirdPlugin - Unload
09:28:13.102 [main] DEBUG DefaultPluginManager - Plugin Unloaded: Plugin{id='ThirdPlugin', version=0.0.8-SNAPSHOT}
```

## 6. Licence
Copyright 2017-2023 Arild G. Gjerd, Luke Sosnicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
