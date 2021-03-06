package org.jqassistant.contrib.plugin.hcl.parser.model.terraform;

import java.io.File;
import java.nio.file.Path;

import org.jqassistant.contrib.plugin.hcl.model.TerraformDescriptor;
import org.jqassistant.contrib.plugin.hcl.model.TerraformLogicalModule;
import org.jqassistant.contrib.plugin.hcl.util.StoreHelper;

import com.google.common.collect.ImmutableMap;

/**
 * A structure which does not exist in terraform. Groups all objects of one
 * directory.
 *
 * @author Matthias
 * @since 1.0
 */
public class LogicalModule extends TerraformObject<TerraformLogicalModule> {
  /**
   * Calculates the full qualified name for a logical module.
   *
   * @param directory the path name of the file this module is defined in
   * @return A name which can be used as ID
   */
  public static String calculateFullQualifiedName(final Path directory) {
    return directory.normalize().toString().replace(File.separatorChar, '.');
  }

  private final String name;

  public LogicalModule(final String name) {
    this.name = name;
  }

  @Override
  protected TerraformLogicalModule saveInternalState(final TerraformLogicalModule object,
      final TerraformLogicalModule partOfModule, final Path filePath, final StoreHelper storeHelper) {
    final String fullQualifiedName = calculateFullQualifiedName(filePath.getParent());

    final TerraformLogicalModule module = storeHelper.createOrRetrieveObject(
        ImmutableMap.of(TerraformDescriptor.FieldName.FULL_QUALIFIED_NAME, fullQualifiedName),
        TerraformLogicalModule.class);

    module.setFullQualifiedName(fullQualifiedName);
    module.setInternalName(this.name);
    module.setName(this.name);

    return module;
  }
}
