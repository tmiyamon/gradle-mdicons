package com.tmiyamon.mdicons.repository

import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.ext.pathJoin
import org.jetbrains.spek.api.Spek
import java.io.File
import kotlin.test.assertEquals

class MaterialDesignIconsSpecs: Spek() { init {
    given("MaterialDesignIcons") {

        val repository = MaterialDesignIcons(File("/tmp"))
        val filename = "ic_camera_white_16dp.png"

        on(".convertToDensityDirNameFrom") {
            it("returns density dir name with density") {
                assertEquals(
                        MaterialDesignIcons.convertToDensityDirNameFrom("mdpi"),
                        "${MaterialDesignIcons.DENSITY_DIR_NAME_PREFIX}mdpi"
                        )
            }
        }

        on(".parseFileName") {
            it("parses file name into name, color, size and ext") {
                assertEquals(
                        MaterialDesignIcons.parseFileName(filename),
                        listOf("ic_camera", "white", "16dp", "png")
                )
            }
        }

        on(".buildFileName") {
            it("builds file name with name, color, size and ext") {
                assertEquals(
                        MaterialDesignIcons.buildFileName("a","b","c","d"),
                        "a_b_c.d"
                )
            }
        }

        on("#eachIconDir") {
            it("traverses each icon directories with specific categories and densities") {

                val categories = arrayOf("a", "b")
                val densities = arrayOf("c", "d")
                val expected = listOf(
                    listOf("a","c", File(repository.rootDir, pathJoin("a", "drawable-c"))),
                    listOf("a","d", File(repository.rootDir, pathJoin("a", "drawable-d"))),
                    listOf("b","c", File(repository.rootDir, pathJoin("b", "drawable-c"))),
                    listOf("b","d", File(repository.rootDir, pathJoin("b", "drawable-d")))
                )
                var i = 0

                val block = { c: String, d: String, dir: File ->
                    assertEquals(listOf(c, d, dir), expected[i++])
                }
                repository.eachIconDir(categories, densities, block)
            }
        }

        on("#newIcon") {
            it("returns new icon with category, density and file name") {
                val icon = repository.newIcon("a", "b", filename)
                assertEquals(icon.category, "a")
                assertEquals(icon.density, "b")
                assertEquals(icon.name, "ic_camera")
                assertEquals(icon.color, "white")
                assertEquals(icon.size, "16dp")
                assertEquals(icon.ext, "png")
            }
        }

    }

    given("Icon") {
        val repository = MaterialDesignIcons(File("/tmp"))
        val filename = "ic_camera_white_16dp.png"
        val icon = repository.newIcon("a", "b", filename)

        on("#getFileName") {
            it("returns file name") {
                assertEquals(icon.getFileName(), filename)
            }
        }
        on("#getSourceRelativePath") {
            it("returns source relative path") {
                assertEquals(icon.getSourceRelativePath(), pathJoin("a", "drawable-b", filename))
            }
        }

        on("#getDestinationRelativePath") {
            it("returns destination relative path") {
                assertEquals(icon.getDestinationRelativePath(), pathJoin(Extension.PROJECT_RESOURCE_RELATIVE_PATH, "drawable-b", filename))
            }
        }

        on("#toFile") {
            it("returns file") {
                assertEquals(icon.toFile(), File(repository.rootDir, pathJoin("a", "drawable-b", filename)))
            }
        }

        on("#isBaseIcon") {
            it("return true if its color and density match to the base ones") {
                val baseIcon = repository.newIcon(
                        "a",
                        MaterialDesignIcons.BASE_DENSITY,
                        MaterialDesignIcons.buildFileName("b", MaterialDesignIcons.BASE_COLOR, "c", "d")
                )
                assertEquals(true, baseIcon.isBaseIcon())
            }

            it("returns false if its density is not base one") {
                val notBaseIcon = repository.newIcon(
                        "a",
                        "notBaseDensity",
                        MaterialDesignIcons.buildFileName("b", MaterialDesignIcons.BASE_COLOR, "c", "d")
                )
                assertEquals(false, notBaseIcon.isBaseIcon())
            }

            it("returns false if its color is not base one") {
                val notBaseIcon = repository.newIcon(
                        "a",
                        MaterialDesignIcons.BASE_DENSITY,
                        MaterialDesignIcons.buildFileName("b", "notBaseColor", "c", "d")
                )
                assertEquals(false, notBaseIcon.isBaseIcon())
            }
        }

        on("#newIconVariantsForDensitires") {
            it("returns icon variants for densities") {
                assertEquals(
                        icon.newIconVariantsForDensities(arrayOf("a", "b")),
                        listOf(
                                repository.newIcon("a", "a", filename),
                                repository.newIcon("a", "b", filename)
                        )
                )
            }
        }
    }
}}

