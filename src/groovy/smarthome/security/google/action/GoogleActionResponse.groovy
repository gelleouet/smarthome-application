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
	
	
	GoogleActionResponse askQuestion(String text, List intents) {
		expectUserResponse = true
		GoogleActionExpectedInput input = new GoogleActionExpectedInput()
		intents.each {
			input.possibleIntents << new GoogleActionExpectedIntent(intent: it)
		}
		input.inputPrompt = new GoogleActionInputPrompt()
		input.inputPrompt.richInitialPrompt = new GoogleActionRichResponse()
		input.inputPrompt.richInitialPrompt.items << new GoogleActionItem(simpleResponse: new GoogleActionSimpleResponse(
			textToSpeech: text))
		input.inputPrompt.noInputPrompts << new GoogleActionSimpleResponse(textToSpeech: text)
		expectedInputs << input 
		return this
	}
}
