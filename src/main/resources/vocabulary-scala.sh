java_vocabulary=$(find ~/Dropbox/studies/EAR/SPipes Editor -path "*/model/Vocabulary.java")
scala_vocabulary="$(echo $java_vocabulary | rev | cut -d'/' -f2- |rev)/Vocabulary.scala"
echo > $scala_vocabulary
grep package $java_vocabulary | cut -d';' -f1  >> $scala_vocabulary
echo >> $scala_vocabulary
echo "object Vocabulary {" >> $scala_vocabulary
echo >> $scala_vocabulary
grep "String" $java_vocabulary | cut -d' ' -f 9,10,11 | cut -d';' -f1 | awk '{print "  final val "$0}' >> $scala_vocabulary
echo >> $scala_vocabulary
echo "}" >> $scala_vocabulary
rm $java_vocabulary
