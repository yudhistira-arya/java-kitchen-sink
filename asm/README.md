## ASM Overview

What for: load, write, and analyse java `.class` file without the need to use classloader.

2 kind os APIs: 

* Event based: `ClassReader` (read `.class` file) -> `ClassVisitor` (do transformation) -> `ClassWriter` output the 
  transformation
* Tree based: **TODO**

API is based on Visitor pattern (similar to the SAX parsing model).

## Event Based API

Class visitor requires strict order of the methods call: 

1. `visit`
1. `visitSource`
1. `visitOuterClass`
1. (`visitAnnotation | `visitAttribute` )*
1. (`visitInnerClass` | `visitField` | `visitMethod` )*
1. `visitEnd`

## TreeBased API

Similar to JAXB.

**TODO**