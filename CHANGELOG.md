### Story

* [+] Feature
* [-] Deleting functional
* [\*] Improvement
* [!] Bug fixing

2.0.0 (15.01.2021)
====================

* [\*] Upgrade java version to 13
* [!] Fix for unprotected IBM MQ connection (issue #13)
* [!] Fix for server credentials update (issue #13)

1.6.0 (08.01.2021)
====================

* [+] Feature request: IBM MQ support (issue #13)

1.5.0 (04.12.2020)
====================

* [\*] Refactoring based on plugin compatibility verification report
* [\*] org.jetbrains.intellij version update to 0.6.4
* [\*] Idea sandbox version upgrade to 2020.2.3
* [\*] Removing host validation (issue #11)
* [!] IntelliJ Crash (issues #9 and #10)

1.4.0 (12.08.2019)
====================
 * [\*] moving credentials to Idea Credentials storage
 * [\*] update message table in swing thread
 * [\*] update server tree in swing thread
 * [!] fix for loading queues vie configuration panel

1.3.0 (28.07.2019)
====================
 * [!] Fix for overlapping toolbar and drop-down queue browse action (issue #7)
 * [!] Fix configuration window resize on Linux (issue #6)

1.2.0 (10.06.2019)
====================
 * [\*] Validation for host field on server edit panel
 * [!] Fix for destination table update on server edit panel
 
1.1.0 (08.05.2019)
====================
 * [+] Allowing to send nulls as headers values
 * [\*] Fix for server edit panel size
 * [!] Fix for NPE when some message properties are null (issue #4)
 * [!] Fix for server changes saving message

1.0.2 (06.05.2019)
====================
 * [\*] Fix for connection type label (pull request #3)

1.0.1 (19.01.2019)
====================
 * [!] Fix for deserialization RabbitMQ message in case it isn't sent by java client (issue #2)

1.0.0 (14.01.2019)
====================
 * [\*] File dialog windows for ActiveMQ SSL configuration
 
0.9.0 (07.09.2018)
====================
 * [+] SSL configuration for ActiveMQ (issue #1)
 
0.8.0 (01.09.2018)
====================
 * [+] RabbitMQ support

0.7.0 (28.08.2018)
====================
 * [\*] Message view and sending panel refactoring

0.6.0 (20.08.2018)
====================
 * [!] Fix for exception when one of projects gets closed
 
0.5.0 (12.08.2018)
====================
 * [+] Kafka support
 * [\*] Improvement in server edit panel
 * [-] Removing first queue implementation
 
0.4.0 (08.08.2018)
====================
 * [\*] Message payload panel adjustment
 * [\*] Server edit panel adjustment

0.3.0 (05.08.2018)
====================
 * [+] Runnable configuration for sending templates

0.2.1 (02.08.2018)
====================
 * [\*] Support for several versions of ide

0.2.0 (30.07.2018)
====================
 * [+] Support 2017 ide
 * [+] Topic support
 * [+] Templates
 * [\*] Copy paste buttons on payload panel
 * [\*] Improvements in queue browse panel

0.1.1 (22.07.2018)
====================
 * [!] Dependency fix

0.1.0 (22.07.2018)
====================
 * [+] Deleting messages
 * [+] Sending messages
 * [+] ActiveMQ support
 * [+] Artemis support
 * [+] HornetQ support
 * [+] Browse messages
 