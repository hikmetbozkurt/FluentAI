#!/usr/bin/env python3
"""
Applies expansions to data_reading_c2_part2.py to ensure all articles exceed 1120 words cleanly.
"""

with open('tools/curriculum_batch_003/data_reading_c2_part2.py', encoding='utf-8') as f:
    text = f.read()

# Article 6 expansions: + ~200 words
text = text.replace(
    'international stratigraphy."',
    'international stratigraphy. This geological shift signals that humanity can no longer view Earth as an external, passive backdrop to historical drama, but as an active, volatile partner in an intertwined planetary destiny."'
)
text = text.replace(
    'geological arrival."',
    'geological arrival. These radioactive markers provide immutable chronostratigraphic correlation points that will remain chemically distinguishable in continental shelf cores and speleothem deposits across deep geological time."'
)
text = text.replace(
    'commercial organisms."',
    'commercial organisms. The rapid deposition of these synthetic strata demonstrates that industrialization has effectively re-engineered the Earth\'s surface lithology, embedding anthropogenic artifacts into the permanent geological archive."'
)
text = text.replace(
    'ocean chemistry."',
    'ocean chemistry. The simultaneous destabilization of multiple boundary systems threatens to erode the holistic resilience of the planetary biosphere, locking regional ecosystems into irreversible degradation trajectories."'
)
text = text.replace(
    'finite planet."',
    'finite planet. By interrogating the unequal geopolitical architecture of ecological devastation, critics emphasize that addressing planetary collapse requires dismantling the extractive economic hierarchies that profit from environmental exhaustion."'
)
text = text.replace(
    'human civilization."',
    'human civilization. The cascading nature of these tipping elements underscores the urgent necessity of precautionary Earth-system governance to prevent the destabilization of self-reinforcing planetary warming mechanisms."'
)
text = text.replace(
    'on our species."',
    'on our species. Ultimately, aligning human civilization with planetary boundaries requires cultivating a profound ecological humility that honors the biophysical limits of our shared terrestrial home."'
)

# Article 7 expansions: + ~270 words
text = text.replace(
    'democratic responsiveness."',
    'democratic responsiveness. In this rigid institutional environment, procedural compliance is elevated above ethical purpose, alienating public servants from the human populations they were originally chartered to serve."'
)
text = text.replace(
    'institutional mission."',
    'institutional mission. When performance metrics substitute for authentic organizational excellence, institutions become hollowed-out bureaucracies obsessed with statistical optics rather than real-world efficacy and public service."'
)
text = text.replace(
    'administrative optimization."',
    'administrative optimization. This technocratic insulated authority disenfranchises democratic citizens, creating a profound crisis of political alienation wherein communities feel governed by invisible, unaccountable administrative algorithms."'
)
text = text.replace(
    'catastrophic failure occurs."',
    'catastrophic failure occurs. Devaluing the tacit wisdom of frontline practitioners blinds executive leadership to emergent structural flaws, ensuring that institutional decay proceeds unchallenged until catastrophe strikes."'
)
text = text.replace(
    'constitutional democracy itself."',
    'constitutional democracy itself. Rebuilding institutional legitimacy demands confronting the deep inequities engineered by technocratic management, establishing transparent avenues for direct democratic oversight and collective participation."'
)
text = text.replace(
    'digital oligarchy."',
    'digital oligarchy. By replacing human moral discretion with opaque computational telemetry, automated bureaucracy threatens to institutionalize systemic prejudice beneath a deceptive facade of mathematical neutrality."'
)
text = text.replace(
    'increasingly complex world."',
    'increasingly complex world. Reclaiming institutions as democratic commons requires prioritizing human dignity, ethical deliberation, and collective accountability over sterile technocratic optimization."'
)

# Article 8 expansions: + ~200 words
text = text.replace(
    'moral patiency."',
    'moral patiency. Dismantling this anthropocentric conceit compels moral philosophy to confront the complex continuum of agency, intelligence, and sentience that permeates both biological nature and synthetic architectures."'
)
text = text.replace(
    'sociotechnical ecologies."',
    'sociotechnical ecologies. By acknowledging our radical entanglement with technological and non-human actors, posthumanism inspires a more generous, capacious ethical imagination suited to an interconnected planetary reality."'
)
text = text.replace(
    'moral culpability."',
    'moral culpability. This functional moral agency forces society to develop novel legal and philosophical frameworks capable of holding distributed algorithmic networks accountable for catastrophic real-world harms."'
)
text = text.replace(
    'digital atrocities."',
    'digital atrocities. Navigating this epistemic uncertainty requires cultivating profound moral precaution, ensuring that humanity does not blindly replicate historical patterns of exploitation upon newly emerging forms of synthetic agency."'
)
text = text.replace(
    'human-technological ecosystem."',
    'human-technological ecosystem. Grounding moral status in relational practice prevents ethical philosophy from devolving into abstract metaphysical gatekeeping, anchoring ethical obligation in the lived reality of human-machine coexistence."'
)
text = text.replace(
    'automated slaughter."',
    'automated slaughter. Preventing the automation of lethal violence is an urgent moral imperative essential to preserving the foundational tenets of universal human rights and international humanitarian law."'
)
text = text.replace(
    'synthetic cosmos."',
    'synthetic cosmos. Ultimately, embracing posthumanist ethics allows humanity to transcend biological narcissism, cultivating a humble, compassionate stewardship of our deeply entangled technological and ecological future."'
)

# Article 9 expansions: + ~220 words
text = text.replace(
    'boundaries of social reality."',
    'boundaries of social reality. Unmasking these naturalized linguistic codes reveals that language is the ultimate battlefield where competing visions of justice, authority, and human possibility are constantly waged."'
)
text = text.replace(
    'sociopolitical emancipation."',
    'sociopolitical emancipation. By providing citizens with the analytical tools to dissect institutional rhetoric, Critical Discourse Analysis transforms passive readers into critically conscious agents of democratic transformation."'
)
text = text.replace(
    'utopian fantasies."',
    'utopian fantasies. Challenging hegemonic discourse requires exposing the political interests disguised as natural economic laws, opening imaginative space for egalitarian social and ecological alternatives."'
)
text = text.replace(
    'administrative policies."',
    'administrative policies. This deliberate semantic distancing alienates the public from the moral reality of state violence, highlighting the vital necessity of restoring ethical clarity and vivid physical precision to political speech."'
)
text = text.replace(
    'political solidarity."',
    'political solidarity. Through creative semiotic resistance, grassroots movements demonstrate that the linguistic tools of corporate dominance can be inverted and mobilized to advance democratic emancipation."'
)
text = text.replace(
    'mainstream policy imperatives."',
    'mainstream policy imperatives. Reframing public discourse expands the parameters of political imagination, enabling transformative social movements to articulate visionary alternatives to entrenched status-quo dogmas."'
)
text = text.replace(
    'collective emancipation."',
    'collective emancipation. Wielding language with moral courage and poetic imagination remains humanity\'s most resilient defense against authoritarian manipulation and ideological subjugation."'
)

# Article 10 expansions: + ~240 words
text = text.replace(
    'democratic cultural life."',
    'democratic cultural life. This sacred Humboldtian commitment to disinterested scholarship nurtured generations of visionary thinkers whose philosophical, scientific, and cultural contributions fundamentally enriched global civilization."'
)
text = text.replace(
    'across international markets."',
    'across international markets. Subordinating higher education to volatile market forces has fundamentally transformed universities from bastions of civic enlightenment into hyper-commercialized corporate training enterprises."'
)
text = text.replace(
    'upward mobility."',
    'upward mobility. Dismantling the corrosive culture of credentialism requires restoring educational integrity, valuing intellectual exploration and critical consciousness above transactional degree commodification."'
)
text = text.replace(
    'research monoculture."',
    'research monoculture. Starving the humanities of institutional resources cripples a society\'s capacity for ethical reflection, historic self-awareness, and nuanced democratic critique in an increasingly technocratic world."'
)
text = text.replace(
    'intellectual pieceworkers."',
    'intellectual pieceworkers. Ending the exploitative casualization of academic labor is an essential prerequisite for safeguarding academic freedom, scholarly excellence, and institutional memory across global universities."'
)
text = text.replace(
    'private media conglomerates."',
    'private media conglomerates. Freeing academic institutions from the reductive tyranny of commercial rankings allows universities to reclaim their unique regional missions and dedicate scholarship to pressing public needs."'
)
text = text.replace(
    'democratic emancipation."',
    'democratic emancipation. Reclaiming the university as a democratic public good reaffirms our collective commitment to uncompromised truth, intellectual courage, and universal human flourishing."'
)

with open('tools/curriculum_batch_003/data_reading_c2_part2.py', 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated data_reading_c2_part2.py directly.')
