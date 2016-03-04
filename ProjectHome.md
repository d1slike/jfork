# JFork #
## nProperty ##
nProperty is a library for comfortable loading property files (aka ini-files) through annotations, reflections and standard Properties Java API.

You can find full nProperty documentation at:
  * English: http://microfork.com/reading-configuration-files-in-java-nproperty/
  * Russian: http://microfork.com/reading-configuration-files-with-java-nproperty/

### Important Changelogs ###
v1.4
  * Added XML parse/store support (store support implemented manually to save key ordering).

v1.3.1
  * Added recursive parametrization feature.

**Be aware switching to this version!**

Sample ini file:

# ---------------

param = 1

param1 = ${param}

param2 = ${param1}

# ---------------

in v1.3 produces:

// ---------------

param = 1

param1 = 1

param2 = ${param}

// ---------------

in v1.3.1 produces:

// ---------------

param = 1

param1 = 1

param2 = 1

// ---------------

according to new recursive parametrization feature.

---

v1.3
  * Added parametrization feature.

---

v1.2:
  * Added ability to use prefixes in class annotation;
  * Added ability to load properties through java.io.Reader interface with overriding content encoding.