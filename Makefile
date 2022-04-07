setup:
	ln -sf ../../tools/pre-commit.sh .git/hooks/pre-commit
build-all:
	./gradlew bootJar
start: build-all
	docker-compose up --force-recreate
start-in-background: build-all
	docker-compose up --force-recreate -d
logs:
	docker-compose logs --follow
up: start
down:
	docker-compose down
service-tests:
	./gradlew clean test
wait-for-service:
	./tools/wait-for-service.sh
run-e2e:
	./gradlew clean cucumber
e2e-tests: start-in-background wait-for-service run-e2e down
test-request:
	curl -X POST -H "Content-Type: application/json" http://localhost:8080/orders -d @test_request.json
run-perf:
	./gradlew gatlingRun
perf-tests: start-in-background wait-for-service run-perf down
kafka-consume:
	docker exec broker /bin/kafka-console-consumer --topic $(TOPIC) --from-beginning --bootstrap-server localhost:9092