package de.assertagile.gebstandalonedsl

import org.codehaus.groovy.control.CompilerConfiguration
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class GebBaseScriptSpecLocalHost extends Specification {

    @AutoCleanup
    @Shared
    def ratpackServer =
            GroovyEmbeddedApp.of {
                serverConfig {
                    port(0)
                }
                handlers {
                    get {
                        response.contentType('text/html')
                        render "<html><body><a href='https://github.com'>github</a></body></html>"
                    }
                }
            }

    def "evaluate script calling localhost via GroovyShell"() {
        given:
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration(scriptBaseClass: 'de.assertagile.gebstandalonedsl.GebBaseScript')
        GroovyShell shell = new GroovyShell(this.class.classLoader, compilerConfiguration)
        URI ratpackUri = ratpackServer.address
        expect:
        shell.evaluate("""import org.openqa.selenium.chrome.ChromeDriver
            browser ChromeDriver // .openqa.selenium.firefox.FirefoxDriver
            baseUrl "${ratpackUri}"
            go("/")
            assert \$("a").find { it.attr("href").startsWith("https://github.com") }
            """.stripIndent()) == null
    }


}
