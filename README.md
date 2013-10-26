jcastulo
========

jcastulo is a Shoutcast server implemented with Java

Features
- Allows to create several mount point which are names streams
- Allows to add mp3 files to every stream
- Re-encoding the mp3 files before they are send to the clients (reduce the bitrate)
- Tested with Winamp as Shoutcast client

Frameworks used
- Spring Framework 3.2.3
- Hibernate 4.2.0
- JPA 2.0
- JAudio tagger 2.0.3
- Xuggler 5.3

Coming fixes/changes
- Configuration properties file that includes
  * Port
  * Domain (if not specified then use IP address)
  * Log location (if not specified then use the same dir)
  * datasource info (if not specified then use H2) option for javaDB files should be in the same dir
- Test what happens if port is deny to be opened