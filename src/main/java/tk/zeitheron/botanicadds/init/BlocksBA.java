package tk.zeitheron.botanicadds.init;

import tk.zeitheron.botanicadds.blocks.*;
import vazkii.botania.common.lexicon.LexiconData;

public class BlocksBA
{
	public static final BlockManaTesseract MANA_TESSERACT = new BlockManaTesseract();
	public static final BlockTerraCatalyst TERRA_CATALYST = new BlockTerraCatalyst();
	public static final BlockDummy DREAMROCK = new BlockDummy("dreamrock").setLexiconEntry(() -> LexiconData.elvenResources);
	public static final BlockDummy MANA_LAPIS_BLOCK = new BlockDummy("mana_lapis_block").setLexiconEntry(() -> LexiconData.pool);
	public static final BlockDummy ELVEN_LAPIS_BLOCK = new BlockDummy("elven_lapis_block").setLexiconEntry(() -> LexiconData.elvenResources);
	public static final BlockElvenwoodLog ELVENWOOD_LOG = new BlockElvenwoodLog();
	public static final BlockElvenAltar ELVEN_ALTAR = new BlockElvenAltar();
	public static final BlockDreamingPool DREAMING_POOL = new BlockDreamingPool();
	public static final BlockGaiaPlate GAIA_PLATE = new BlockGaiaPlate();
	public static final BlockDecorativeMetal GAIASTEEL_BLOCK = new BlockDecorativeMetal("gaiasteel_block", "blockGaiasteel");
}