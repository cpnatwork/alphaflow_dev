##### KEY FILES/DIRECTORIES
AVV_SDIR	:= src/main/java/alpha
AVV_TDIR	:= src/test/java/alpha
TOC			:= toc
TEST_MK		:= .test


##### HYDRA SOURCES & TESTS
hsources	:= $(shell find $(SRC_DIR) -name '*.java')
tsources	:= $(shell find $(TEST_DIR) -name '*.java')

#################################################
############## PRIMARY TARGET ###################
all: $(TOC) $(TEST_MK)

$(TOC): $(hsources) $(tsources)
	@echo "##### Updating the Table of Contents"
	@echo "##### AFlow VVS Source #####" > $(TOC)
	@find $(AVV_SDIR) -name '*.java' | sort >> $(TOC)
	@echo "" >> $(TOC)
	@echo "##### AFlow VVS Tests #####" >> $(TOC)
	@find $(AVV_TDIR) -name '*.java' | sort >> $(TOC)
	@echo "" >> $(TOC)
	@echo "##### Other Files #####" >> $(TOC)
	@echo "makefile" >> $(TOC)
	@echo "pom.xml" >> $(TOC)

$(TEST_MK): $(hsrouces) $(tsources)
	mvn clean
	mvn test -e -Dmaven.test.skip=false
