package smarthome.security.google.action

class GoogleActionResponse {
	String conversationToken
	boolean expectUserResponse
	List<GoogleActionExpectedInput> expectedInputs = []
	GoogleActionFinalResponse finalResponse
	boolean isInSandbox
	def customPushMessage
	def responseMetadata
	
	
	GoogleActionResponse endConversation(String text) {
		expectUserResponse = false
		finalResponse = new GoogleActionFinalResponse()
		finalResponse.richResponse = new GoogleActionRichResponse()
		finalResponse.richResponse.items << new GoogleActionItem(simpleResponse: new GoogleActionSimpleResponse(
			textToSpeech: text))
		return this	
	}
	
	
	GoogleActionResponse askQuestion(String text, List suggestions = []) {
		expectUserResponse = true
		GoogleActionExpectedInput input = new GoogleActionExpectedInput()
		input.possibleIntents << new GoogleActionExpectedIntent(intent: "actions.intent.TEXT")
		input.inputPrompt = new GoogleActionInputPrompt()
		input.inputPrompt.richInitialPrompt = new GoogleActionRichResponse()
		input.inputPrompt.richInitialPrompt.items << new GoogleActionItem(simpleResponse: new GoogleActionSimpleResponse(
			textToSpeech: text))
		suggestions?.each { suggestion ->
			input.inputPrompt.richInitialPrompt.suggestions << new GoogleActionSuggestion(title: suggestion)
		}
		//input.inputPrompt.noInputPrompts << new GoogleActionSimpleResponse(textToSpeech: text)
		expectedInputs << input 
		return this
	}
}
