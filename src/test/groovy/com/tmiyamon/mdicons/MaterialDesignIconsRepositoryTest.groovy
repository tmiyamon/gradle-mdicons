package com.tmiyamon.mdicons

import com.tmiyamon.mdicons.mdicons.MaterialDesignIconsRepository
import mockit.Expectations
import mockit.Mock
import mockit.Mocked
import mockit.NonStrictExpectations
import mockit.Verifications
import mockit.integration.junit4.JMockit
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import spock.lang.Specification

/**
 * Created by tmiyamon on 5/1/15.
 */
@RunWith(JMockit.class)
class MaterialDesignIconsRepositoryTest {
    def repository
    @Mocked private Closure closure

    @Before
    void setUp() {
        repository = new MaterialDesignIconsRepository("/")
    }

    @Test
    void "#eachIconDir traverses each icon dir"() {
        new Expectations() {{
//            closure.call('action', 'mdpi', new File('/action/drawable-mdpi'))
//            closure.call('camera', 'mdpi', new File('/camera/drawable-mdpi'))
//            closure.call('action', 'hdpi', new File('/action/drawable-hdpi'))
//            closure.call('camer', 'hdpi', new File('/camera/drawable-hdpi'))
            closure.call(); times = 5
        }}
        repository.eachIconDir(['action','camera'], ['mdpi', 'hdpi'], closure)

    }
//
//    def "#eachIconFile traverses each icon file"() {
//        setup:
//        new NonStrictExpectations(File.class) {{
//            new File(any).listFiles(); result = ['a.png', 'b.png']
//        }}
//
//        when:
//        repository.eachIconFile(['action'], ['mdpi'], closure)
//
//        then:
//        1 * closure.call('action', 'mdpi', new File('/action/drawable-mdpi/a.png'))
//        1 * closure.call('action', 'mdpi', new File('/action/drawable-mdpi/b.png'))
//    }


}
