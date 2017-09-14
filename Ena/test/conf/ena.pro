#
# This ProGuard configuration file illustrates how to process ProGuard itself.
# Configuration files for typical applications will be very similar.
# Usage:
#     java -jar proguard.jar @proguard.pro
#

# Specify the input jars, output jars, and library jars.
# We'll filter out the Ant and WTK classes, keeping everything else.

-injars  ena_in.jar
-outjars ena.jar

-libraryjars <java.home>/lib/rt.jar

# Write out an obfuscation mapping file, for de-obfuscating any stack traces
# later on, or for incremental obfuscation of extensions.

-printmapping proguard.map

# Allow methods with the same signature, except for the return type,
# to get the same obfuscation name.

-overloadaggressively

# Put all obfuscated classes into the nameless root package.

-repackageclasses 'ena'

# Allow classes and class members to be made public.

-allowaccessmodification

# The entry point: ProGuard and its main method.

-keep public class com.bridge.ena.cs.command.CommandFactory{
	public *;
}
-keep public class com.bridge.ena.vs.command.CommandFactory{
	public *;
}
-keep public interface com.bridge.ena.xmlrpc.IXMLRPCClient{
	public *;
}
-keep public class com.bridge.ena.xmlrpc.XMLRPCClient{
	public *;
}
-keep public class com.bridge.ena.cs.command.AbstractCSCommand{
	public *;
}
-keep public class com.bridge.ena.vs.command.AbstractVSCommand{
	public *;
}
-keep public class com.bridge.ena.cs.command.TariffedAdjustmentCommand{
	public *;
}
-keep public class com.bridge.ena.cs.adjustment.policy.DedicatedAccountAdjustments{
	public *;
}
-keep public class com.bridge.ena.cs.adjustment.policy.AdjustmentBase{
	public *;
}
-keep public class com.bridge.ena.xml.BadXmlFormatException{
	public *;
}
-keep public class com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException{
	public *;
}

#-keep public class * {
#    public *;
#}

#-keepclassmembers public class * {
#   public *;
#}

# If you want to preserve the Ant task as well, you'll have to specify the
# main ant.jar.

#-libraryjars /usr/local/java/ant1.6.5/lib/ant.jar
#-keep public class proguard.ant.* {
#    public void set*(***);
#    public void add*(***);
#}


# If you want to preserve the WTK obfuscation plug-in, you'll have to specify
# the kenv.zip file.

#-libraryjars /usr/local/java/wtk2.1/wtklib/kenv.zip
#-keep public class proguard.wtk.ProGuardObfuscator
