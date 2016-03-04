##  Written 1999 by Douglas Greiman.
## 
##  This software may be used and distributed according to the terms
##  of the GNU Public License, incorporated herein by reference.
##

## Constants
JAVAC = javac
JAVACFLAGS = -classpath ../.. -g -target 1.1
JAVA = java
JVIEW = jview
JVIEWFLAGS = /d:JIT=none /vst
JAR = jar
JEXEGEN = jexegen
JEXEGENFLAGS = /v /w

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

# Generic Rules
%.class : %.java
	$(JAVAC) $(JAVACFLAGS) $<

%.jar : %.mft
	cd $(PACKAGEPATHUP) ; $(JAR) cvfm $(PACKAGEPATH)$@ $(PACKAGEPATH)$< $(PACKAGEPATH)*.class

# Targets for this project
all: exe

jar: $(JARFILE)

exe: $(EXEFILE)

$(JARFILE) : $(OBJS) $(MANIFEST)

$(EXEFILE): $(OBJS)
	$(JEXEGEN) $(JEXEGENFLAGS) /out:$(EXEFILE) /base:$(PACKAGEPATHUP) /main:duggelz.jape.JapeGui $(PACKAGEPATH)*.class

run: run-exe

run-exe: $(EXEFILE)
	./$(EXEFILE)

run-jview: $(JARFILE)
	$(JVIEW) $(JVIEWFLAGS) /cp:a $(JARFILE) $(PACKAGEPATH)$(MAINCLASS)

run-java: $(JARFILE)
	$(JAVA) $(JAVAFLAGS) -jar $(JARFILE)

run-obj: $(OBJS)
	$(JAVA) $(JAVAFLAGS) -classpath ../.. duggelz.jape.JapeGui

clean:
	-rm *.class $(JARFILE) $(EXEFILE) 2>/dev/null
