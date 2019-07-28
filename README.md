### Jms-messenger

#### Current version 1.3.0

#### Repository
 [https://github.com/klyashko/jms-messenger](https://github.com/klyashko/jms-messenger)
 
#### Plugin page
 [https://plugins.jetbrains.com/plugin/10949-jms-messenger](https://plugins.jetbrains.com/plugin/10949-jms-messenger)
 
#### Description
 This is a plugin for Intellij idea which provides jms support.
 Plugin supports:
   * browsing messages
   * deleting messages
   * sending messages

#### Plugin Compatibility
 Plugin required java jdk 1.8 or higher and version of ide 2017 or higher
 
#### Usages
 Plugin designed to be used for development or testing purposes

#### Supported jms providers:
 * Wildfly 10.x.x or higher (Artemis)
 * Artemis (via HTTP or TCP)
 * ActiveMq (via TCP or SSL)
 * Wildfly 9.x.x (HornetQ)
 * HornetQ (via HTTP)
 * RabbitMQ (via TCP)
 * Kafka (via TCP)
 
#### Limitation  
 Only sending messages is supported for kafka.
 Only text messages may be send or browse.
 Authorization is not supported for kafka.
 
#### Suggestions and bug reports
 In case of finding bug or having a suggestion about new features or supported providers you can open a ticket on
[Github](https://github.com/klyashko/jms-messenger/issues) or contact me directly on email kipill_90@mail.ru
