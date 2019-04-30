package de.assertagile.gebstandalonedsl

import org.codehaus.groovy.control.CompilerConfiguration
import org.openqa.selenium.firefox.FirefoxDriver
import spock.lang.Specification

class GebBaseScriptSpec extends Specification {

    def "evaluate script via GroovyShell"() {
        given:
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration(scriptBaseClass: 'de.assertagile.gebstandalonedsl.GebBaseScript')
        GroovyShell shell = new GroovyShell(this.class.classLoader, compilerConfiguration)

        expect:
        shell.evaluate('''
            browser org.openqa.selenium.firefox.FirefoxDriver
            baseUrl "http://assertagile.de"
            go("/")
            assert $("a").find { it.attr("href").startsWith("https://github.com") }
            '''.stripIndent()) == null
    }

    def "direct initialization"() {
        given:
        GebBaseScript baseScript = new GebBaseScript()

        expect:
        baseScript.with {
            browser FirefoxDriver
            baseUrl "http://assertagile.de"
            drive {
                go "/"
                assert $("a").find { it.attr("href").startsWith("https://github.com") }
            }
        }
    }
}
