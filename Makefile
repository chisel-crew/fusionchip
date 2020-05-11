show:	
clean:
	@find . -name "target" | xargs rm -rf {} \;

vs:
	@rm -rf .bloop .metals project/.bloop

rm:clean 
	@echo
