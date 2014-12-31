# This makefile is defined to give you the following targets:
#
#    default: Same as check
#    make-check: Run integration tests on make application.
#    make-trip: Run integration tests on trip application.
#    check: Same as make-check + make-trip 
#    clean: Remove extraneous files.
#
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

SHELL = /bin/bash

HERE := $(shell pwd)

GRAPH_CLASSES := $(HERE)/graph-classes
MAKE_CLASSES := $(HERE)/make-classes
TRIP_CLASSES := $(HERE)/trip-classes

ifeq ($(GRAPHPACK),staff)
$(info ***Using staff graph package***)
GRAPH_CLASSES = $(HERE)/staff-graph.jar
endif

ifeq ($(MAKEPACK),staff)
$(info ***Using staff make package***)
MAKE_CLASSES := $(HERE)/staff-make.jar
endif

ifeq ($(TRIPPACK),staff)
$(info ***Using staff trip package***)
TRIP_CLASSES := $(HERE)/staff-trip.jar
endif

MAKE_CLASSPATH = $(GRAPH_CLASSES):$(MAKE_CLASSES):$(CLASSPATH)

TRIP_CLASSPATH = $(GRAPH_CLASSES):$(TRIP_CLASSES):$(CLASSPATH)

TESTER := python3 tester.py

TESTER_FLAGS =

.PHONY: default check clean make-check trip-check \
	staff-make-check staff-trip-check \
	staff-graph-make-check staff-graph-trip-check

# First, and therefore default, target.
default:
	$(MAKE) -C .. 
	$(MAKE) check

check: make-check trip-check

make-check: $(GRAPH_CLASSES) $(MAKE_CLASSES)
	@echo "Testing application make..."
	@cd make; CLASSPATH=$(MAKE_CLASSPATH) $(TESTER) $(TESTER_FLAGS) *.in

trip-check: $(GRAPH_CLASSES) $(TRIP_CLASSES)
	@echo "Testing application trip..."
	@cd trip; CLASSPATH=$(TRIP_CLASSPATH) $(TESTER) $(TESTER_FLAGS) *.in

$(HERE)/staff-graph.jar $(HERE)/staff-make.jar $(HERE)/staff-trip.jar:
	@base=$(notdir $@); if [ -f $(HERE)/../$$base ]; then \
	    $(RM) $@; \
	    ln -s ../$$base .; \
	elif [ -f $(MASTERDIR)/lib/$$base ]; then \
	    $(RM) $@; \
	    ln -s $(MASTERDIR)/lib/$$base .; \
	else \
	    echo Could not find $$base; \
	fi

# 'make clean' will clean up stuff you can reconstruct.
clean:
	cd trip; $(RM) -r *~ __pycache__ 
	cd make; $(RM) -r *~ __pycache__ 
	$(RM) staff-*.jar

