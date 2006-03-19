package net.sf.anathema.character.db;

import net.sf.anathema.character.db.additional.AdditionalCultDbRules;
import net.sf.anathema.character.db.additional.AdditionalDbRules;
import net.sf.anathema.character.db.additional.AdditionalOutcasteDbRules;
import net.sf.anathema.character.db.additional.AdditionalSequesteredTabernacleDbRules;
import net.sf.anathema.character.db.additional.BasicAdditionalLookshyDbRules;
import net.sf.anathema.character.db.additional.NativeLookshyDbRules;
import net.sf.anathema.character.db.reporting.DbVoidStateReportTemplate;
import net.sf.anathema.character.db.template.IDbSpecialCharms;
import net.sf.anathema.character.db.template.cult.KetherRockDbTemplate;
import net.sf.anathema.character.db.template.cult.SequesteredTabernacleDbTemplate;
import net.sf.anathema.character.db.template.dynastic.DynasticDbTemplate;
import net.sf.anathema.character.db.template.dynastic.ImmaculateDbTemplate;
import net.sf.anathema.character.db.template.lookshy.LookshyDbTemplate;
import net.sf.anathema.character.db.template.lookshy.LookshyOutcasteDbTemplate;
import net.sf.anathema.character.db.template.lookshy.LookshyRealmDbTemplate;
import net.sf.anathema.character.db.template.outcaste.LowerClassOutcasteDbTemplate;
import net.sf.anathema.character.db.template.outcaste.PatricianOutcasteDBTemplate;
import net.sf.anathema.character.db.template.outcaste.ThresholdOutcasteDbTemplate;
import net.sf.anathema.character.db.template.pirates.PirateOutcasteDbTemplate;
import net.sf.anathema.character.db.template.pirates.PirateRealmDbTemplate;
import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.module.CharacterGenericsModuleAdapter;
import net.sf.anathema.character.generic.impl.additional.NullAdditionalRules;
import net.sf.anathema.character.generic.impl.backgrounds.TemplateTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.magic.persistence.CharmCache;
import net.sf.anathema.character.generic.magic.charms.special.ISpecialCharm;
import net.sf.anathema.character.generic.template.ITemplateRegistry;
import net.sf.anathema.character.generic.template.TemplateType;
import net.sf.anathema.character.generic.traits.LowerableState;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.logging.Logger;
import net.sf.anathema.lib.registry.IIdentificateRegistry;
import net.sf.anathema.lib.resources.IResources;

public class DbCharacterModule extends CharacterGenericsModuleAdapter {

  private final Logger logger = Logger.getLogger(DbCharacterModule.class);
  public static final String BACKGROUND_ID_ARSENAL = "Arsenal"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_BREEDING = "Breeding"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_FAMILY = "Family"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_COMMAND = "Command"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_CONNECTIONS = "Connections"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_HENCHMEN = "Henchmen"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_ILLUMINATION = "Illumination"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_REPUTATION = "Reputation"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_RETAINERS = "Retainers"; //$NON-NLS-1$
  public static final String BACKGROUND_ID_SORCERY = "Sorcery"; //$NON-NLS-1$
  private final AdditionalDbRules additionalDbRules = new AdditionalDbRules();
  private final AdditionalDbRules immaculateDbRules = new AdditionalDbRules();
  private final NativeLookshyDbRules nativeLookshyDbRules = new NativeLookshyDbRules();
  private final BasicAdditionalLookshyDbRules realmLookshyDbRules = new BasicAdditionalLookshyDbRules();
  private final AdditionalOutcasteDbRules outcasteDbRules = new AdditionalOutcasteDbRules();
  private final AdditionalCultDbRules cultRules = new AdditionalCultDbRules();
  private final AdditionalSequesteredTabernacleDbRules sequesteredTabernacleRules = new AdditionalSequesteredTabernacleDbRules();

  @Override
  public void registerCommonData(ICharacterGenerics characterGenerics) {
    ISpecialCharm[] specialCharms = new ISpecialCharm[] { IDbSpecialCharms.OX_BODY_TECHNIQUE };
    characterGenerics.getCharmProvider().setSpecialCharms(CharacterType.DB, specialCharms);
  }

  @Override
  public void addCharacterTemplates(ICharacterGenerics characterGenerics) {
    CharmCache charmProvider = CharmCache.getInstance();
    registerDbTemplate(characterGenerics.getTemplateRegistry(), charmProvider);
  }

  @Override
  public void addBackgroundTemplates(ICharacterGenerics generics) {
    IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry = generics.getBackgroundRegistry();
    TemplateType[] allDbTemplateType = new TemplateType[] {
        DynasticDbTemplate.TEMPLATE_TYPE,
        PatricianOutcasteDBTemplate.TEMPLATE_TYPE,
        LowerClassOutcasteDbTemplate.TEMPLATE_TYPE,
        LookshyDbTemplate.TEMPLATE_TYPE,
        LookshyOutcasteDbTemplate.TEMPLATE_TYPE,
        LookshyRealmDbTemplate.TEMPLATE_TYPE,
        ImmaculateDbTemplate.IMMACULATE_TEMPLATE_TYPE,
        PirateOutcasteDbTemplate.TEMPLATE_TYPE,
        PirateRealmDbTemplate.TEMPLATE_TYPE };
    TemplateTypeBackgroundTemplate breedingTemplate = new TemplateTypeBackgroundTemplate(
        BACKGROUND_ID_BREEDING,
        allDbTemplateType,
        LowerableState.Immutable);
    backgroundRegistry.add(breedingTemplate);
    additionalDbRules.addBreedingRules(breedingTemplate);
    immaculateDbRules.addBreedingRules(breedingTemplate);
    outcasteDbRules.addBreedingRules(breedingTemplate);
    nativeLookshyDbRules.addBreedingRules(breedingTemplate);
    realmLookshyDbRules.addBreedingRules(breedingTemplate);
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_COMMAND, allDbTemplateType));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_CONNECTIONS, allDbTemplateType));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_FAMILY, new TemplateType[] {
        DynasticDbTemplate.TEMPLATE_TYPE,
        LookshyDbTemplate.TEMPLATE_TYPE,
        LookshyRealmDbTemplate.TEMPLATE_TYPE }));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_HENCHMEN, allDbTemplateType));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_REPUTATION, allDbTemplateType));
    addLookshyBackgrounds(backgroundRegistry);
    addCultBackgrounds(backgroundRegistry);
    addSorcery(backgroundRegistry);
  }

  private void addCultBackgrounds(IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry) {
    TemplateType[] cultTemplates = new TemplateType[] {
        KetherRockDbTemplate.TEMPLATE_TYPE,
        SequesteredTabernacleDbTemplate.TEMPLATE_TYPE };
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_ILLUMINATION, cultTemplates));
  }

  private void addSorcery(IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry) {
    TemplateTypeBackgroundTemplate sorceryBackground = new TemplateTypeBackgroundTemplate(
        BACKGROUND_ID_SORCERY,
        new TemplateType[] {
            DynasticDbTemplate.TEMPLATE_TYPE,
            LookshyDbTemplate.TEMPLATE_TYPE,
            LookshyOutcasteDbTemplate.TEMPLATE_TYPE,
            LookshyRealmDbTemplate.TEMPLATE_TYPE,
            KetherRockDbTemplate.TEMPLATE_TYPE,
            SequesteredTabernacleDbTemplate.TEMPLATE_TYPE },
        LowerableState.Immutable);
    backgroundRegistry.add(sorceryBackground);
    nativeLookshyDbRules.addSorceryRules(sorceryBackground);
    realmLookshyDbRules.addSorceryRules(sorceryBackground);
    additionalDbRules.addSorceryRules(sorceryBackground);
    cultRules.addSorceryRules(sorceryBackground);
    sequesteredTabernacleRules.addSorceryRules(sorceryBackground);
  }

  private void addLookshyBackgrounds(IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry) {
    TemplateType[] lookshyTemplateTypes = new TemplateType[] {
        LookshyDbTemplate.TEMPLATE_TYPE,
        LookshyOutcasteDbTemplate.TEMPLATE_TYPE,
        LookshyRealmDbTemplate.TEMPLATE_TYPE };
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_ARSENAL, lookshyTemplateTypes));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_RETAINERS, lookshyTemplateTypes));
  }

  private void registerDbTemplate(ITemplateRegistry templateRegistry, CharmCache charmProvider) {
    try {
      templateRegistry.register(new DynasticDbTemplate(charmProvider, additionalDbRules));
      templateRegistry.register(new ImmaculateDbTemplate(charmProvider, immaculateDbRules));
      templateRegistry.register(new PatricianOutcasteDBTemplate(charmProvider, outcasteDbRules));
      templateRegistry.register(new LowerClassOutcasteDbTemplate(charmProvider, outcasteDbRules));
      templateRegistry.register(new ThresholdOutcasteDbTemplate(charmProvider, new NullAdditionalRules()));
      templateRegistry.register(new LookshyDbTemplate(charmProvider, nativeLookshyDbRules));
      templateRegistry.register(new LookshyOutcasteDbTemplate(charmProvider, nativeLookshyDbRules));
      templateRegistry.register(new LookshyRealmDbTemplate(charmProvider, realmLookshyDbRules));
      templateRegistry.register(new PirateOutcasteDbTemplate(charmProvider, outcasteDbRules));
      templateRegistry.register(new PirateRealmDbTemplate(charmProvider, additionalDbRules));
      templateRegistry.register(new KetherRockDbTemplate(charmProvider, cultRules));
      templateRegistry.register(new SequesteredTabernacleDbTemplate(charmProvider, sequesteredTabernacleRules));
    }
    catch (PersistenceException exception) {
      logger.error("Dragon-Blooded Charms not found", exception); //$NON-NLS-1$
    }
  }

  @Override
  public void addReportTemplates(ICharacterGenerics generics, IResources resources) {
    generics.getReportTemplateRegistry().add(new DbVoidStateReportTemplate(resources));
  }
}