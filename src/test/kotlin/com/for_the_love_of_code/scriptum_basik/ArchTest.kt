package com.for_the_love_of_code.scriptum_basik

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.for_the_love_of_code.scriptum_basik")

        noClasses()
            .that()
                .resideInAnyPackage("com.for_the_love_of_code.scriptum_basik.service..")
            .or()
                .resideInAnyPackage("com.for_the_love_of_code.scriptum_basik.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.for_the_love_of_code.scriptum_basik.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses)
    }
}
