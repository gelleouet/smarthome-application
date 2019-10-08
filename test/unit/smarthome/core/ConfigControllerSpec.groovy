package smarthome.core



import grails.test.mixin.*
import spock.lang.*

@TestFor(ConfigController)
@Mock(Config)
class ConfigControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.configInstanceList
            model.configInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.configInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def config = new Config()
            config.validate()
            controller.save(config)

        then:"The create view is rendered again with the correct model"
            model.configInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            config = new Config(params)

            controller.save(config)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/config/show/1'
            controller.flash.message != null
            Config.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def config = new Config(params)
            controller.show(config)

        then:"A model is populated containing the domain instance"
            model.configInstance == config
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def config = new Config(params)
            controller.edit(config)

        then:"A model is populated containing the domain instance"
            model.configInstance == config
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/config/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def config = new Config()
            config.validate()
            controller.update(config)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.configInstance == config

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            config = new Config(params).save(flush: true)
            controller.update(config)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/config/show/$config.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/config/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def config = new Config(params).save(flush: true)

        then:"It exists"
            Config.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(config)

        then:"The instance is deleted"
            Config.count() == 0
            response.redirectedUrl == '/config/index'
            flash.message != null
    }
}
