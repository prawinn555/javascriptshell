# javascriptshell
How to write a complex shell script in Javascript :)

# JVM

This project can work with GraalVM and classic VM.

For GraalMV,
Configure JRE System Library to GraalVM (just download and unzip to any directory).

#






# Annexe (Docker)


configure volume.
If we execute "docker inspect jsshell"
We will see

<pre>


        "Mounts": [
            {
                "Type": "bind",
                "Source": "/host_mnt/c/dev/volume/jsshell/workspace",
                "Destination": "/home/workspace",
                "Mode": "",
                "RW": true,
                "Propagation": "rprivate"
            }
        ],

</pre>

If we put files on C:/dev/volume/jsshell/workspace => il will be accessible via /home/workspace