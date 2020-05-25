# javascriptshell
How to write a complex shell script in Javascript :)

# JVM

This project can work with GraalVM and classic VM.

For GraalMV,
Configure JRE System Library to GraalVM (just download and unzip to any directory).


# Annexe (Docker)


Understand volume

<pre>

docker run ..  -v myvol2:/app ...
  
"Mounts": [
    {
        "Type": "volume",
        "Name": "myvol2",  => given name
        "Source": "/var/lib/docker/volumes/myvol2/_data",  => location @ host machine 
        "Destination": "/app", => destination @ the container.
        ... 
    }
],

</pre>