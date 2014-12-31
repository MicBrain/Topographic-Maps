# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run tests with
#     make check

# This is not an especially efficient Makefile, because it's not easy to
# figure out the minimal set of Java files that need to be recompiled.  
# So if any .class file does not exist or is older than its .java file,
# we just remove all the .class files, compile the main class, and 
# then compile everything in the plugin directory.  

SHELL = bash 

STYLEPROG = style61b

# Tell make that these are not really files.
.PHONY: clean default check style jar dist

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default:
	$(MAKE) -C graph
	$(MAKE) -C trip
	$(MAKE) -C make

all: default jar

check: default
	$(MAKE) -C graph check
	$(MAKE) -k -C testing check

# Check style of source files.
style: $(CLASSES)
	$(MAKE) STYLEPROG=$(STYLEPROG) -C graph style
	$(MAKE) STYLEPROG=$(STYLEPROG) -C trip style
	$(MAKE) STYLEPROG=$(STYLEPROG) -C make style

# Find and remove all *~, *.class, and testing output files.
# Do not touch .svn directories.
clean :
	$(RM) *~
	$(MAKE) -C graph clean
	$(MAKE) -C trip clean
	$(MAKE) -C make clean
