package net.sf.anathema.character.solar.generic;

import net.sf.anathema.character.generic.framework.magic.AbstractGenericCharm;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.type.ShortCharmTypeStringBuilder;
import net.sf.anathema.character.generic.impl.magic.charm.type.CharmTypeModel;
import net.sf.anathema.character.generic.impl.rules.ExaltedSourceBook;
import net.sf.anathema.character.generic.magic.charms.duration.SimpleDuration;
import net.sf.anathema.character.generic.magic.charms.type.CharmType;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.lib.resources.IResources;

public class EssenceFlow extends AbstractGenericCharm {

  @Override
  protected String getId() {
    return "Solar.EssenceFlow"; //$NON-NLS-1$
  }

  @Override
  protected ExaltedSourceBook getSourceBook() {
    return ExaltedSourceBook.SecondEdition;
  }

  @Override
  protected boolean isComboOk() {
    return false;
  }

  public String getCostString(IResources resources) {
    return "-"; //$NON-NLS-1$
  }

  public String getDurationString(IResources resources) {
    return SimpleDuration.PERMANENT_DURATION.getText(resources);
  }
  
  public ICharacterType getCharacterType()
  {
	  return CharacterType.SOLAR;
  }

  public String getType(IResources resources) {
    CharmTypeModel model = new CharmTypeModel();
    model.setCharmType(CharmType.Permanent);
    return new ShortCharmTypeStringBuilder(resources).createTypeString(model);
  }
}