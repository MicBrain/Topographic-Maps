#!/usr/bin/env python3
# -*-Python-*-

import testing
import os, sys, re
import io
import getopt
from os.path import join, exists

PROGRAM = "java -ea make.Main"

def unwindow(text):
    """Remove bogus carriage returns from TEXT."""
    return re.sub(r'\r', '', text)

def ordered(lines):
    def tag(L):
        m = re.search(r';\s*([\d\s*]*)', L)
        if m:
            return re.split(r'\s+', m.group(1).strip())
        else:
            return []
    def ok(L0, L1):
        if not L0 or not L1:
            return True
        if len(L0) != len(L1):
            return False
        for c0, c1 in zip(L0, L1):
            if c0 != '*' and c1 != '*' and int(c0) > int(c1):
                return False
        return True

    for k, line0 in enumerate(lines):
        for line1 in lines[k+1:]:
            if not ok(tag(line0), tag(line1)):
                return False
    return True

class Proj3_Make_Tester(testing.Tester):
    """A tester for the make application.  Each test ID.in represents the test
          java -ea make.Maih -f ID.mk -D ID.dir `cat ID.in`
    where ID.in contains the targets to be built for this test.  
    The resulting standard output is compared to ID.out, after filtering out
    leading and trailing whitespace and blank lines."""

    def to_lines(self, text):
        """Return TEXT broken into lines."""
        return re.split(r'\r?\n', text.rstrip())

    def command_args(self, testid):
        with open(testid) as inp:
            targets = inp.read().strip()
        return "-f {id}.mk -D {id}.dir {targets}"\
               .format(id=self.base_id(testid), targets=targets)

    def input_files(self, id):
        name = self.base_id(id)
        result = ()
        for ext in ".mk", ".dir", ".in":
            f = name + ext
            fullname = join(self.base_dir(id), f)
            if exists(fullname):
                result += ((f, fullname, None),)
        return result

    def error_filter(self, id, text):
        """Convert error messages to the phrase "<SOME ERROR MESSAGE>"
        in TEXT."""
        return re.sub(r'(?im)^.*\berror\b.*', '<SOME ERROR MESSAGE>',
                      unwindow(text))

    def output_compare(self, testid):
        """Sets .reason to True iff either:
            a. The RC is 0, there is no .err file for this test, and the
               filtered .stdout contents are equal to the filtered standard
               file; or
            b. The RC is non-zero, there is a .err file for this test, and
               the filtered error output equals the filtered standard error
               output.
        and otherwise sets .reason to error message."""

        self.reason = True
        if self.standard_error_file(testid):
            if self.rc == 0:
                self.reason = "Program did not detect error---exit code 0."
            else:
                std_err_output = \
                      testing.contents(self.standard_error_file(testid))
                if self.stderr is None or \
                   self.error_filter(testid, self.stderr) != std_err_output:
                    self.reason = "Missing error message or extra error output"
        elif self.rc != 0:
            self.reason = "Execution error---exit code not 0."
        else:
            std = self.to_lines(testing.contents(self.standard_output_file
                                                 (testid)))
            test_out = self.to_lines(self.stdout)
            if set(std) != set(test_out):
                self.reason = "Wrong set of rebuilding command issued"
            elif not ordered(test_out):
                self.reason = "Rebuilding commands have invalid ordering"

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 trip-tester.py [--show=N] TEST...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj3_Make_Tester(tested_program=PROGRAM, report_limit=show)

sys.exit(0 if tester.test_all(args) else 1)

