rootProject.name = "microservices-demo"
include("services:catalogue")
include("services:customers")
include("services:email")
include("services:orders")
include("services:payments")
include("end-to-end-tests")
include("performance-tests")
include("common")