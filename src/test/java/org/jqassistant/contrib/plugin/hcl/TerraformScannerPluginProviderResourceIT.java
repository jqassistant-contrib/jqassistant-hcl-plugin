package org.jqassistant.contrib.plugin.hcl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.io.File;
import java.util.Map;

import org.jqassistant.contrib.plugin.hcl.model.TerraformFileDescriptor;
import org.jqassistant.contrib.plugin.hcl.test.AbstractTerraformPluginIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;

public class TerraformScannerPluginProviderResourceIT extends AbstractTerraformPluginIT {
    private static final String FILE_EGRESS_TF = "/terraform/provider resource/egress.tf";
    private static final String FILE_INGRESS_TF = "/terraform/provider resource/ingress.tf";
    private static final String FILE_TF = "/terraform/provider resource/main.tf";
    private static final String FILE1_TF = "/terraform/provider resource/different_providers.tf";

    @Test
    public void shouldReadAllAttributePerResource_whenScan_givenProviderResource() {
        // given
        final File givenTestFile = new File(this.getClassesDirectory(TerraformScannerPluginProviderResourceIT.class),
                FILE_TF);

        // when
        final TerraformFileDescriptor actualDescriptor = this.getScanner().scan(givenTestFile, FILE_TF,
                DefaultScope.NONE);

        // then
        assertThat(actualDescriptor.isValid()).isTrue();

        assertThat(actualDescriptor.getModule().getProviderResources())
                .filteredOn(pr -> "aws_instance".equals(pr.getType())).hasSize(1).first().satisfies(pr -> {
                    final Map<String, String> actualProperties = readAllProperties(pr);

                    assertThat(actualProperties).containsOnly(entry("ami", "data.aws_ami.ami.id"),
                            entry("instance_type", "t2.micro"), entry("provider", "aws"),
                            entry("security_groups", "[aws_security_group.server.name]"),
                            entry("tags", "{Name=\"my server\"}"), entry("type", "aws_instance"),
                            entry("internalName", "server"),
                            entry("fullQualifiedName", ".terraform.provider resource.aws_instance.server"),
                            entry("name", "server"));
                });
    }

    @Test
    public void shouldReadAllResources_whenScan_givenProviderResources() {
        // given
        final File givenTestFile = new File(this.getClassesDirectory(TerraformScannerPluginProviderResourceIT.class),
                FILE_TF);

        // when
        final TerraformFileDescriptor actualDescriptor = this.getScanner().scan(givenTestFile, FILE_TF,
                DefaultScope.NONE);

        // then
        assertThat(actualDescriptor.isValid()).isTrue();
        assertThat(actualDescriptor.getModule().getProviderResources()).hasSize(3);
    }

    @Test
    public void shouldReadAllResources_whenScan_givenResourcesFromDifferentProviders() {
        // given
        final File givenTestFile = new File(this.getClassesDirectory(TerraformScannerPluginProviderResourceIT.class),
                FILE1_TF);

        // when
        final TerraformFileDescriptor actualDescriptor = this.getScanner().scan(givenTestFile, FILE1_TF,
                DefaultScope.NONE);

        // then
        assertThat(actualDescriptor.isValid()).isTrue();
        assertThat(actualDescriptor.getModule().getProviderResources()).hasSize(2);
    }

    @Test
    public void shouldReadEgressBlocks_whenScan_givenAwsSecurityGroup() {
        // given
        final File givenTestFile = new File(this.getClassesDirectory(TerraformScannerPluginProviderResourceIT.class),
                FILE_EGRESS_TF);

        // when
        final TerraformFileDescriptor actualDescriptor = this.getScanner().scan(givenTestFile, FILE_EGRESS_TF,
                DefaultScope.NONE);

        // then
        assertThat(actualDescriptor.isValid()).isTrue();
        assertThat(actualDescriptor.getModule().getProviderResources())
                .filteredOn(pr -> "aws_security_group".equals(pr.getType())).hasSize(1).first().satisfies(pr -> {
                    final Map<String, String> actualProperties = readAllProperties(pr);

                    assertThat(actualProperties.get("egress")).isNotNull();
                });
    }

    @Test
    public void shouldReadIngressBlocks_whenScan_givenAwsSecurityGroup() {
        // given
        final File givenTestFile = new File(this.getClassesDirectory(TerraformScannerPluginProviderResourceIT.class),
                FILE_INGRESS_TF);

        // when
        final TerraformFileDescriptor actualDescriptor = this.getScanner().scan(givenTestFile, FILE_INGRESS_TF,
                DefaultScope.NONE);

        // then
        assertThat(actualDescriptor.isValid()).isTrue();
        assertThat(actualDescriptor.getModule().getProviderResources())
                .filteredOn(pr -> "aws_security_group".equals(pr.getType())).hasSize(1).first().satisfies(pr -> {
                    final Map<String, String> actualProperties = readAllProperties(pr);

                    assertThat(actualProperties.get("ingress")).isNotNull();
                });
    }

    @BeforeEach
    public void startTransaction() {
        this.store.beginTransaction();
    }
}
