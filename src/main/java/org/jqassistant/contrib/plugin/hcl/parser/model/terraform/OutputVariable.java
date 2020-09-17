package org.jqassistant.contrib.plugin.hcl.parser.model.terraform;

import java.util.ArrayList;
import java.util.List;

import org.jqassistant.contrib.plugin.hcl.model.TerraformBlock;
import org.jqassistant.contrib.plugin.hcl.model.TerraformOutputVariable;
import org.jqassistant.contrib.plugin.hcl.util.StoreHelper;

import com.buschmais.jqassistant.core.store.api.Store;
import com.google.common.collect.ImmutableMap;

public class OutputVariable extends TerraformObject {
  private final List<String> dependentObjects = new ArrayList<String>();

  private String description;

  private String name;

  private String sensitive;

  private String value;

  public void addDependentObject(final String object) {
    this.dependentObjects.add(object);
  }

  public String getName() {
    return this.name;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setSensitive(final String sensitive) {
    this.sensitive = sensitive;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  /**
   * Converts this object into a {@link TerraformOutputVariable} and puts it into
   * the store.
   *
   * @param storeHelper helper to access the {@link Store}
   * @return <code>variable</code>
   */
  public TerraformOutputVariable toStore(final StoreHelper storeHelper) {
    final TerraformOutputVariable variable = storeHelper.createOrRetrieveObject(
        ImmutableMap.of(TerraformOutputVariable.FieldName.NAME, this.name), TerraformOutputVariable.class);

    variable.setDescription(this.description);
    variable.setName(this.name);
    variable.setSensitive(this.sensitive);
    variable.setValue(this.value);

    this.dependentObjects.forEach(dependentObjectName -> {
      final TerraformBlock block = storeHelper.createOrRetrieveObject(
          ImmutableMap.of(TerraformBlock.FieldName.FULL_QUALIFIED_NAME, dependentObjectName), TerraformBlock.class);
      block.setFullQualifiedName(dependentObjectName);

      variable.getDependantObjects().add(block);
    });

    return variable;
  }
}