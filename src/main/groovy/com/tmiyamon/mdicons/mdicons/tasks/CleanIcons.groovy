//package com.tmiyamon.mdicons.mdicons.tasks
//
//import com.tmiyamon.mdicons.mdicons.Evaluator
//import org.gradle.api.Project
//
///**
// * Created by tmiyamon on 4/26/15.
// */
//public class CleanIcons extends AbstractTaskWrapper {
//    CleanIcons(Project project) {
//        super(project)
//    }
//
//    def doOnAfterEvaluate(Evaluator evaluator) {
//        task.inputs.property("copiedFilePaths", evaluator.copiedFilePaths)
//        task.doLast {
//            task.project.delete(evaluator.copiedFilePaths as List)
//        }
//
////        if (evaluator.configChanged && evaluator.cacheDir.isDirectory()) {
////            Map<String, List<Icon>> iconMapping = [:].withDefault {[]}
////            Icon.eachProjectResourceIcons(evaluator.resourceDir) { Icon icon ->
////                iconMapping[icon.newCanonical().fileName] << icon
////            }
////            Icon.eachCacheCanonicalIcons(evaluator.cacheDir) { Icon icon ->
////                iconMapping[icon.fileName].each {
////                    it.getProjectResourceVariantFiles(evaluator.resourceDir).each { it.delete() }
////                }
////            }
////        }
//    }
//}
