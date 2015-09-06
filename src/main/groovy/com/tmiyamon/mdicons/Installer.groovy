package com.tmiyamon.mdicons

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class Installer {
    final List<Asset> assets
    final MaterialDesignIconsRepository repository
    final Map<String, ColorHex> colorIndex
    final AndroidProject targetProject

    static Installer create(List<Asset> assets, MaterialDesignIconsRepository repository, Map<String, ColorHex> colorIndex, AndroidProject project) {
        new Installer(assets, repository, colorIndex, project)
    }

    private Installer(List<Asset> assets, MaterialDesignIconsRepository repository, Map<String, ColorHex> colorIndex, AndroidProject targetProject) {
        this.assets = assets
        this.repository = repository
        this.colorIndex = colorIndex
        this.targetProject = targetProject
    }

    def install() {
        Utils.workAt(repository.workDir) { File workingDir ->
            assets
                .collect { repository.listIcons(it) }
                .flatten()
                .toSet()
                .groupBy { it.density }
                .each { density, icons ->
                def iconDir = targetProject.resMdiconsDirOf(density)
                if (!iconDir.isDirectory()) {
                    iconDir.mkdirs()
                }

                targetProject.copy {
                    from icons.collect { getOrCreateFileAt(it, workingDir) }
                    into iconDir
                }
            }
        }
    }

    File createFileAt(MaterialDesignIconsRepository.Icon icon, File workDir) {
        def workFile = icon.toFile(workDir)

        if (!workFile.isFile()) {
            if (!colorIndex.containsKey(icon.color)) {
                throw new IllegalArgumentException("Not found color \"${icon.color}\". You can list color definition by listColors task.")
            }

            BufferedImage baseIconImage = ImageIO.read(icon.newWithColor("white").toFile(repository.rootDir))
            int w = baseIconImage.width
            int h = baseIconImage.height

            BufferedImage newIconImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

            (0..w-1).each { x ->
                (0..h-1).each { y ->
                    int[] pixel = baseIconImage.getRaster().getPixel(x, y, null as int[])
                    int color = pixel[0]
                    int alpha = pixel[1]

                    newIconImage.getRaster().setPixel(x, y, colorIndex[icon.color].rgba(alpha) as int[])
                }
            }

            workFile.parentFile.mkdirs()
            ImageIO.write(newIconImage, "png", workFile)
        }

        workFile
    }

    File getOrCreateFileAt(MaterialDesignIconsRepository.Icon icon, File workDir) {
        def file = icon.toFile(repository.rootDir)

        if (file.isFile()) {
            file
        } else {
            createFileAt(icon, workDir)
        }
    }
}
