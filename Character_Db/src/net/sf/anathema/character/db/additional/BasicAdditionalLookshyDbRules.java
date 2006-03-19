package net.sf.anathema.character.db.additional;

import java.util.ArrayList;

import net.sf.anathema.character.db.DbCharacterModule;
import net.sf.anathema.character.generic.additionalrules.ITraitCostModifier;
import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.module.MortalCharacterModule;
import net.sf.anathema.character.generic.impl.additional.DefaultAdditionalRules;
import net.sf.anathema.character.generic.impl.additional.DefaultTraitCostModifier;
import net.sf.anathema.character.generic.traits.ITraitType;

public class BasicAdditionalLookshyDbRules extends DefaultAdditionalRules {

  public BasicAdditionalLookshyDbRules() {
    super(new ArrayList<String>() {
      {
        add(MortalCharacterModule.BACKGROUND_ID_CONTACTS);
        add(MortalCharacterModule.BACKGROUND_ID_INFLUENCE);
        add(MortalCharacterModule.BACKGROUND_ID_FOLLOWERS);
        add(DbCharacterModule.BACKGROUND_ID_HENCHMEN);
      }
    });
  }

  public void addSorceryRules(IBackgroundTemplate sorceryTemplate) {
    addMagicLearnPool(new LookshySorceryLearnPool(sorceryTemplate));
  }

  public void addBreedingRules(IBackgroundTemplate breedingTemplate) {
    addEssencePool(new BreedingEssencePool(breedingTemplate));
  }

  @Override
  public ITraitCostModifier getCostModifier(ITraitType type) {
    final ITraitCostModifier[] modifier = new ITraitCostModifier[] { super.getCostModifier(type) };
    type.accept(new TraitTypeAdapter() {
      @Override
      public void visitBackground(IBackgroundTemplate template) {
        if (template.getId().equals(DbCharacterModule.BACKGROUND_ID_BREEDING)) {
          modifier[0] = new DefaultTraitCostModifier() {
            @Override
            public int getAdditionalDotsToSpend(int traitValue) {
              return Math.max(0, traitValue - 2);
            }
          };
        }
      }
    });
    return modifier[0];
  }
}