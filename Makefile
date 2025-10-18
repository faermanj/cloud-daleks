.PHONY: make

make:
	scripts/make-images.sh

build:
	./mvnw

native:
	scripts/make-native.sh

corretto:
	scripts/make-jre.sh