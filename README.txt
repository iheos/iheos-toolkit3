README

MAJOR OBJECTS (Planned for V3)

Toolkit

A singleton that describes the toolkit environment as a whole.  Built
by a builder.  The current class Initialization is the seed for this class.

Under Tomcat, the singleton is created by the servlet initialization. When
the JVM is started outside the servlet environment (dashboard or unit
test environments), the singleton must still be created.

Creation options:
    - No parameters - grab toolkit.properties from resources
    - File parameter - location of toolkit.properties
    - builder.withXXX("yyy") - initialize builder from code (unit test)

builder.get() returns singleton.  The first time it is called, the
operating environment is validated and errors generated.
instance.getErrors() can be called to retrieve errors.

The toolkit object controls the loading/editing of toolkit.properties and
the location of the external cache.

The idea of warhome as a variable should be removed using resources
instead.

Session (user session)

Built from code already in ToolkitServiceImpl. Most code should not
know about this object. Session object is obtained from factory or
toolkit. Pick one.  Session maintains things like current testsession,
environment. File uploads go to the session. There should be an
immediate request context call to move, not copy, the upload to the request
object.

Request (user request within a session)

Most of what is in session moves to request.

TestEngine

Bundles test service manager down to Xdstest2. Toolkit is parm to builder.
Has no or a default session and request. Built with a builder.
Session and request are optional parameters to the builder. Toolkit
is a required parameter.

TestRequest

Request to operate the test engine.  TestEngine is a parameter to
the builder. Test engine is multi-threaded and test request keeps
all the per-request info.

