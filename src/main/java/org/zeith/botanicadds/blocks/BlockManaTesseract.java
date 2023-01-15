package org.zeith.botanicadds.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.zeith.botanicadds.BotanicAdditions;
import org.zeith.botanicadds.init.ItemsBA;
import org.zeith.botanicadds.tiles.TileManaTesseract;
import org.zeith.botanicadds.world.WorldTesseractData;
import org.zeith.hammerlib.api.blocks.ICustomBlockItem;
import org.zeith.hammerlib.api.forge.BlockAPI;
import org.zeith.hammerlib.core.adapter.BlockHarvestAdapter;
import org.zeith.hammerlib.util.java.Cast;

import java.util.List;
import java.util.Optional;

import static org.zeith.botanicadds.BotanicAdditions.TAB;

public class BlockManaTesseract
		extends SimpleBlockBA
		implements EntityBlock, ICustomBlockItem
{
	public static final EnumProperty<WorldTesseractData.TesseractMode> MODE = EnumProperty.create("mode", WorldTesseractData.TesseractMode.class);
	
	public BlockManaTesseract()
	{
		super(Properties.of(Material.STONE).strength(2.5F).requiresCorrectToolForDrops());
		BlockHarvestAdapter.bindTool(BlockHarvestAdapter.MineableType.PICKAXE, Tiers.IRON, this);
	}
	
	public static Optional<String> getChannel(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().contains("Channel", Tag.TAG_STRING)
				? Optional.of(stack.getTag().getString("Channel"))
				: Optional.empty();
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MODE);
	}
	
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity ent, ItemStack stack)
	{
		getChannel(stack).ifPresent(ch ->
				Cast.optionally(level.getBlockEntity(pos), TileManaTesseract.class)
						.ifPresent(tess ->
						{
							tess.isPrivate = ItemsBA.TESSERACT_ATTUNER.isPrivate(stack);
							tess.channelReadable = ch;
							if(ent instanceof Player pl)
							{
								tess.owner = pl.getGameProfile();
								tess.channelOwnerName = tess.owner.getName();
							}
						})
		);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		if(getChannel(ctx.getItemInHand()).isEmpty())
		{
			var pl = ctx.getPlayer();
			if(pl != null && !pl.level.isClientSide)
			{
				pl.displayClientMessage(Component.translatable("info." + BotanicAdditions.MOD_ID + ".mana_tesseract.no_channel")
								.withStyle(Style.EMPTY.withColor(0xA30000)),
						true
				);
			}
			
			return null;
		}
		
		return super.getStateForPlacement(ctx);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		final var item = new ItemStack(this);
		
		var savedItem = Cast.optionally(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY), TileManaTesseract.class)
				.map(t -> t.storeData(item))
				.orElse(item);
		
		return List.of(savedItem);
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
	{
		final var item = new ItemStack(this);
		
		return Cast.optionally(level.getBlockEntity(pos), TileManaTesseract.class)
				.map(t -> t.storeData(item))
				.orElse(item);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new TileManaTesseract(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_)
	{
		return BlockAPI.ticker();
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return new BlockItem(this, new Item.Properties().tab(TAB))
		{
			@Override
			public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
			{
				getChannel(stack).ifPresent(channel ->
				{
					tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".mana_tesseract.channel",
											Component.literal(channel)
													.withStyle(Style.EMPTY.withColor(0x00A56B))
									)
									.withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
					);
				});
				
				int modeColor = 0x22AA22;
				String mode = "public";
				
				if(ItemsBA.TESSERACT_ATTUNER.isPrivate(stack))
				{
					modeColor = 0xAA2222;
					mode = "private";
				}
				
				tooltip.add(Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode",
								Component.translatable("info." + BotanicAdditions.MOD_ID + ".tesseract_attuner.mode." + mode)
										.withStyle(Style.EMPTY.withColor(modeColor))
						).withStyle(Style.EMPTY.withItalic(true).withColor(0x444444))
				);
			}
		};
	}
}