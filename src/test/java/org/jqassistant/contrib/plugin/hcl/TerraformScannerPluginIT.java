package org.jqassistant.contrib.plugin.hcl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.jqassistant.contrib.plugin.hcl.model.TerraformFileDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.plugin.common.test.AbstractPluginIT;

public class TerraformScannerPluginIT extends AbstractPluginIT {
  private static final String FILE_TEST_TF = "/terraform/main.tf";

  @BeforeEach
  public void beginTransaction() {
    this.store.beginTransaction();
  }

  @Test
  public void shouldScanTerraformFile() {
    final File testFile = new File(this.getClassesDirectory(TerraformScannerPluginIT.class), FILE_TEST_TF);

    final TerraformFileDescriptor descriptor = this.getScanner().scan(testFile, FILE_TEST_TF, DefaultScope.NONE);

    assertThat(descriptor.isValid()).isTrue();
    assertThat(descriptor.getName()).isEqualTo("main.tf");
  }
}
