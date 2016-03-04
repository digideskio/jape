##  Written 1999 by Douglas Greiman.
## 
##  This software may be used and distributed according to the terms
##  of the GNU Public License, incorporated herein by reference.
##

## Constants
JAVAC = javac
JAVACFLAGS = -classpath ../.. -g -target 1.5 -source 1.5
JAVA = java
JVIEW = jview
JVIEWFLAGS = /d:JIT=none /vst
JAR = jar
JEXEGEN = jexegen
JEXEGENFLAGS = /v /w
ZIP = zip
ZIPFLAGS = -X

## Source and object files
SOURCES = JapeGui.java JapeFrame.java JapeAbout.java StatPanel.java ItemPanel.java ItemDetailPanel.java \
	FieldView.java TextView.java NumberView.java ChoiceView.java ItemView.java \
	DataChangeMixin.java DataChangeListener.java DataChangeEvent.java \
	InsetPanel.java OptionDialog.java \
	Actor.java Mercenary.java SaveGame.java Skill.java Item.java ItemExemplar.java \
	Field.java ByteField.java ShortField.java IntField.java StringField.java ChoiceField.java \
	JapeConst.java JapeAlg.java 


# This isn't the compelete list of class files
# It doesn't include all those damn little inner class files
OBJS = $(SOURCES:.java=.class)

# JAR file stuff
MANIFEST = Jape.mft
JARFILE = Jape.jar
EXEFILE = Jape.exe
MAINCLASS = JapeGui.class
PACKAGEPATH = duggelz/jape/
PACKAGEPATHUP = ../..
ZIPFILE = JAPE041.ZIP
SRCZIPFILE = JAPE041SRC.ZIP

# Generic Rules
%.class : %.java
	$(JAVAC) $(JAVACFLAGS) $<

# Couldn't figure out how to get this to work on Windows, so
# wrote a helper batch file.
# cd $(PACKAGEPATHUP) ; $(JAR) cvfm $(PACKAGEPATH)$@ $(PACKAGEPATH)$< $(PACKAGEPATH)*.class
%.jar : %.mft
	-rm $@
	jarhelper.bat

# Targets for this project
all: jar zip

jar: $(JARFILE)

zip: $(ZIPFILE) $(SRCZIPFILE)

exe: $(EXEFILE)

$(JARFILE) : $(OBJS) $(MANIFEST)

$(EXEFILE): $(OBJS)
	$(JEXEGEN) $(JEXEGENFLAGS) /out:$(EXEFILE) /base:$(PACKAGEPATHUP) /main:duggelz.jape.JapeGui $(PACKAGEPATH)*.class

$(ZIPFILE) : $(JARFILE) Jape.html Jape.txt
	-rm $(ZIPFILE)
	$(ZIP) $(ZIPFLAGS) $(ZIPFILE) $^

$(SRCZIPFILE) : *.java *.html *.txt Makefile *.bat
	-rm $(SRCZIPFILE)
	$(ZIP) $(ZIPFLAGS) $(SRCZIPFILE) $^

test: $(OBJS)
	java -classpath $(PACKAGEPATHUP) duggelz.jape.SaveGame

run: run-java

run-exe: $(EXEFILE)
	./$(EXEFILE)

run-jview: $(JARFILE)
	$(JVIEW) $(JVIEWFLAGS) /cp:a $(JARFILE) $(PACKAGEPATH)$(MAINCLASS)

run-java: $(JARFILE)
	$(JAVA) $(JAVAFLAGS) -jar $(JARFILE)

run-obj: $(OBJS)
	$(JAVA) $(JAVAFLAGS) -classpath ../.. duggelz.jape.JapeGui

clean:
	-rm *.class $(JARFILE) $(EXEFILE) $(ZIPFILE) $(SRCZIPFILE)

