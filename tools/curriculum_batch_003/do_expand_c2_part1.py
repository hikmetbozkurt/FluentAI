#!/usr/bin/env python3
"""
Applies paragraph expansions to data_reading_c2_part1.py to ensure all articles exceed 1100 words cleanly.
"""

with open('tools/curriculum_batch_003/data_reading_c2_part1.py', encoding='utf-8') as f:
    text = f.read()

# Article 1 expansions: + ~270 words
text = text.replace(
    'computational panopticon."',
    'computational panopticon. Unlike Bentham\'s physical masonry, digital dataveillance operates without palpable boundaries or acoustic friction, quietly recording sub-perceptual physiological cues that betray private emotions long before conscious cognition formulates them into deliberate speech."'
)
text = text.replace(
    'private capital accumulation."',
    'private capital accumulation. This extractive behavioral architecture establishes an unprecedented dynamic where human beings are no longer merely customers or laborers, but the captive biological terrain from which commercial platform cartels extract raw behavioral telemetry."'
)
text = text.replace(
    'commercial platforms."',
    'commercial platforms. By manufacturing synthetic desires and nudging behavioral trajectories beneath conscious awareness, platform architectures instantiate an insidious psychological servitude disguised as autonomous consumer self-actualization."'
)
text = text.replace(
    'an algorithmic pageant."',
    'an algorithmic pageant. This systematic fragmentation of the civic sphere dismantles the shared factual baseline required for democratic consensus, rendering collective governance vulnerable to weaponized populist agitation."'
)
text = text.replace(
    'fundamental human sovereignty."',
    'fundamental human sovereignty. Reclaiming epistemic sovereignty requires recognizing that cognitive sanctuary is an non-negotiable constitutional precondition without which democratic agency cannot meaningfully exist."'
)
text = text.replace(
    'grounded in human dignity."',
    'grounded in human dignity. In confronting this algorithmic apparatus, humanity defends not merely individual informational privacy, but the sacred right to live an open-ended, un-engineered, and authentic human life."'
)

# Article 2 expansions: + ~300 words
text = text.replace(
    'systemic looting of colonized societies."',
    'systemic looting of colonized societies. By presenting stolen ancestral patrimony under the dispassionate taxonomy of universal human achievement, imperial institutions systematically cleansed plunder of its violent military origins, converting historical theft into cultural virtue."'
)
text = text.replace(
    'social Darwinist evolutionary schemas."',
    'social Darwinist evolutionary schemas. This violent rupture between material craftsmanship and living cultural memory severed succeeding generations from the sacred artistic genealogy of their ancestors, inflicting enduring spiritual wounds across colonized communities."'
)
text = text.replace(
    'sovereign cultural patrimony."',
    'sovereign cultural patrimony. Preserving looted heritage behind fortified European museum walls under the guise of custodial guardianship perpetuates colonial power hierarchies, denying originating nations their sovereign right to curate and venerate their ancestral treasures."'
)
text = text.replace(
    'sovereign African nations."',
    'sovereign African nations. These landmark bilateral treaties demonstrate that genuine historical reconciliation cannot be achieved through rhetorical apologies alone, but requires the unconditional physical restitution of stolen sovereign property."'
)
text = text.replace(
    'traditional customary law."',
    'traditional customary law. By restoring ancestral objects to their sacred living contexts, decolonization transforms restitution from a bureaucratic legal transaction into a profound act of spiritual and communal rebirth."'
)
text = text.replace(
    'grounded in restorative justice."',
    'grounded in restorative justice. In embracing this polycentric future, global museums become vibrant crucibles of authentic cultural dialogue, honoring the living dignity of all human civilizations with intellectual humility and moral clarity."'
)

# Article 3 expansions: + ~280 words
text = text.replace(
    'grammar of classical rationalism."',
    'grammar of classical rationalism. The classical conceit of an objective, independent clockwork universe dissolved into mathematical formalisms where physical outcomes remain fundamentally probabilistic until collapsed by measurement interactions."'
)
text = text.replace(
    'dwarfs all classical machinery."',
    'dwarfs all classical machinery. By manipulating the delicate interference patterns of probability amplitudes across entangled Hilbert spaces, quantum algorithms can evaluate astronomical combinatorial possibilities simultaneously, bypassing classical algorithmic bottlenecks."'
)
text = text.replace(
    'classical notions of physical separation."',
    'classical notions of physical separation. This profound non-local interconnectedness demonstrates that physical isolation is a perceptual illusion, suggesting that the cosmos constitutes an undivided quantum informational tapestry."'
)
text = text.replace(
    'fault-tolerant hardware arrives."',
    'fault-tolerant hardware arrives. This urgent cryptographic transition highlights how theoretical breakthroughs in subatomic mathematics can instantly destabilize the institutional and economic foundations of modern digital civilization."'
)
text = text.replace(
    'pathogens with atomic precision."',
    'pathogens with atomic precision. Native quantum simulation promises to liberate molecular engineering from costly physical trial-and-error, inaugurating an era of direct atomic design that addresses existential climate and healthcare challenges."'
)
text = text.replace(
    'metaphysical secrets of existence."',
    'metaphysical secrets of existence. By decoding the quantum language through which the cosmos computes its own evolution, humanity awakens to its profound role as conscious co-creators of cosmic information."'
)

# Article 4 expansions: + ~300 words
text = text.replace(
    'predictable temporal rhythms and durable communal solidarity."',
    'predictable temporal rhythms and durable communal solidarity. This institutional stability provided workers with the emotional and material security required to plan multi-decade familial investments, anchoring democratic stability in shared economic prosperity."'
)
text = text.replace(
    'shoulders of individual workers."',
    'shoulders of individual workers. Under this punitive regime, corporate profitability was systematically decoupled from domestic labor welfare, leaving millions of workers exposed to the unforgiving headwinds of global capital mobility."'
)
text = text.replace(
    'occupational narrative and institutional belonging."',
    'occupational narrative and institutional belonging. Trapped in a perpetual cycle of temporary contracts and intermittent gig work, precariat laborers are systematically stripped of occupational identity, professional pride, and institutional representation."'
)
text = text.replace(
    'against scapegoated immigrant populations."',
    'against scapegoated immigrant populations. This toxic alchemy of existential dread and political marginalization transforms justified economic frustration into xenophobic hostility, destabilizing the fragile foundations of constitutional democracy."'
)
text = text.replace(
    'written explanation, or due process."',
    'written explanation, or due process. This total subordination of labor to opaque computational telemetry exposes the fundamental fallacy of platform flexibility, revealing it as an automated architecture of algorithmic exploitation."'
)
text = text.replace(
    'genuine democratic self-determination."',
    'genuine democratic self-determination. By establishing an inalienable economic floor, society restores dignity to labor, emancipating citizens from coercive survival contracts and revitalizing the democratic promise of universal human flourishing."'
)

# Article 5 expansions: + ~230 words
text = text.replace(
    'perceptual categorization, and spatial orientation."',
    'perceptual categorization, and spatial orientation. Far from being a passive labeling system, the grammatical architecture of language operates as an active cognitive prism that predisposes native speakers toward specific interpretive horizons."'
)
text = text.replace(
    'neurological perception."',
    'neurological perception. These cross-cultural neuroimaging experiments prove that linguistic categories do not merely describe sensory experience post-hoc, but actively calibrate the speed and precision of cortical visual processing circuits."'
)
text = text.replace(
    'structures our cognitive processing."',
    'structures our cognitive processing. This pervasive metaphorical mapping reveals that human cognition relies fundamentally on imaginative cross-domain projections, demonstrating that rational argumentation is intrinsically shaped by bodily and physical metaphors."'
)
text = text.replace(
    'grounded in biological embodiment."',
    'grounded in biological embodiment. The human mind is not an abstract calculating algorithm isolated within a biological vat, but an embodied organ whose highest philosophical concepts remain inextricably anchored in the physics of flesh and gravity."'
)
text = text.replace(
    'fundamentally intolerable."',
    'fundamentally intolerable. Whoever controls the metaphorical framing of public discourse commands the invisible boundaries of political possibility, dictating which policies appear intuitively natural and which seem unthinkable."'
)
text = text.replace(
    'across civilizations."',
    'across civilizations. In mastering this dynamic symbiosis between language and cognition, we reclaim our agency to dismantle oppressive cognitive frameworks and envision more compassionate, enlightened worlds."'
)

with open('tools/curriculum_batch_003/data_reading_c2_part1.py', 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated data_reading_c2_part1.py directly.')
