#!/usr/bin/env python3
"""
Applies paragraph expansions to data_reading_c1_part3.py to ensure all articles exceed 1000 words cleanly.
"""

with open('tools/curriculum_batch_003/data_reading_c1_part3.py', encoding='utf-8') as f:
    text = f.read()

# Article 9 expansions: + ~220 words
text = text.replace(
    'and severe analytical paralysis."',
    'and severe analytical paralysis. The continuous demand to filter out task-irrelevant environmental stimuli creates persistent neurochemical friction within frontoparietal control networks, accelerating cognitive exhaustion and impairing executive control across all cognitive domains."'
)
text = text.replace(
    'personal inclinations and psychological needs."',
    'personal inclinations and psychological needs. When individuals traverse natural landscapes characterized by gentle sensory fascination, their attentional reserves are restored without requiring active cognitive inhibition, allowing prefrontal neural circuits to consolidate and recover organically."'
)
text = text.replace(
    'windowless, sterile cubicles."',
    'windowless, sterile cubicles. In contrast to sterile architectural environments that elevate chronic stress, biophilic interventions create multi-sensory micro-restorative opportunities throughout the working day, fostering cognitive stamina and psychological equilibrium."'
)
text = text.replace(
    'gently expanding creative associative thinking."',
    'gently expanding creative associative thinking. This synchronized neurobiological relaxation demonstrates that evolutionary exposure to organic environmental patterns is not an arbitrary aesthetic preference, but a deep physiological requirement for sustained human flourishing."'
)
text = text.replace(
    'stresses of modern industrial density."',
    'stresses of modern industrial density. Urban planning that integrates ubiquitous natural infrastructure recognizes that cognitive health is fundamentally tied to spatial accessibility, ensuring that restorative environments are democratized as shared public goods."'
)
text = text.replace(
    'emotional empathy, and creative wisdom."',
    'emotional empathy, and creative wisdom. By defending the restorative commons against digital and commercial encroachment, societies cultivate an essential sanctuary where human beings can replenish their creative potential, emotional balance, and intellectual vitality."'
)

# Article 10 expansions: + ~160 words
text = text.replace(
    'industrialized urban environments."',
    'industrialized urban environments. In the absence of accessible third places, individuals become increasingly atomized within private domestic spaces or rigid workplace hierarchies, depriving civic society of spontaneous, cross-cutting communal bonds."'
)
text = text.replace(
    'anti-authoritarian insurrection."',
    'anti-authoritarian insurrection. This democratic cross-pollination demonstrated that public sociability could flourish beyond traditional religious and aristocratic structures, establishing early prototypes of secular civic culture in the early modern world."'
)
text = text.replace(
    'legitimate democratic exercise."',
    'legitimate democratic exercise. By prioritizing rational critical debate over hereditary noble prestige, the early modern coffeehouse democratized intellectual discourse and laid the communicative groundwork for modern democratic constitutionalism."'
)
text = text.replace(
    'accelerator of early modern civilization."',
    'accelerator of early modern civilization. The convergence of intellectual debate and commercial innovation within coffeehouse culture illustrates how informal public spaces act as generative crucibles for groundbreaking economic and scientific progress."'
)
text = text.replace(
    'doses to hurried commuters."',
    'doses to hurried commuters. The commercial enclosure of contemporary cafes into transactional consumption hubs deprives urban neighborhoods of vital social infrastructure, contributing to the pervasive epidemic of urban isolation and civic disengagement."'
)
text = text.replace(
    'build inclusive societies."',
    'build inclusive societies. Reinvigorating independent communal coffeehouses affirms the enduring democratic power of unhurried human conversation, creating hospitable sanctuaries where diverse citizens can cultivate shared empathy and mutual understanding."'
)

# Article 11 expansions: + ~250 words
text = text.replace(
    'critical information literacy."',
    'critical information literacy. The rapid democratization of digital publishing has dismantled traditional quality assurance mechanisms, placing the immense cognitive burden of factual verification directly onto individual information consumers without institutional support."'
)
text = text.replace(
    'investing cognitive effort in reading the claims."',
    'investing cognitive effort in reading the claims. By validating external institutional credibility before analyzing internal textual rhetoric, lateral reading empowers learners to detect coordinated influence campaigns and astroturfed propaganda before adopting flawed premises."'
)
text = text.replace(
    'epistemic agency."',
    'epistemic agency. Deconstructing algorithmic recommendation systems enables students to recognize that digital feeds reflect commercial engagement optimization rather than comprehensive epistemic reality, safeguarding their cognitive independence against subtle manipulation."'
)
text = text.replace(
    'relentless epistemic vigilance."',
    'relentless epistemic vigilance. Cultivating rigorous epistemic humility when engaging with automated text generation ensures that students remain discerning analytical thinkers rather than passive consumers of computer-generated hallucinations and unverified claims."'
)
text = text.replace(
    'open digital commons."',
    'open digital commons. True epistemic agency empowers citizens to participate actively in democratic self-governance by discerning truth from manipulation, fostering a resilient public sphere capable of collective problem-solving amidst ideological division."'
)
text = text.replace(
    'civic purpose."',
    'civic purpose. Equipping learners with critical information discernment transforms educational institutions into essential bastions of democratic integrity in an increasingly complex and contested information landscape."'
)

# Article 12 expansions: + ~260 words
text = text.replace(
    'ecological constraints."',
    'ecological constraints. By harmonizing building form with local environmental realities, traditional vernacular builders achieved remarkable thermal comfort and structural longevity without consuming depletable fossil fuel resources or generating industrial waste."'
)
text = text.replace(
    'affluent transient visitors."',
    'affluent transient visitors. Uncontrolled tourist commodification transforms authentic living heritage into speculative real estate, displacing the very cultural communities whose ancestral traditions gave historic settlements their unique aesthetic identity."'
)
text = text.replace(
    'paying foreign spectators."',
    'paying foreign spectators. When ancestral traditions are staged primarily for paying external audiences, their sacred communal meaning is hollowed out, reducing living cultural identities into standardized entertainment products for global tourists."'
)
text = text.replace(
    'ancestral construction wisdom survives across generations."',
    'ancestral construction wisdom survives across generations. True heritage conservation honors both material integrity and living craftsmanship, ensuring that ancestral building wisdom continues to be transmitted dynamically across generations rather than preserved solely as inert museum artifacts."'
)
text = text.replace(
    'restoration grants for resident homeowners."',
    'restoration grants for resident homeowners. By retaining tourism revenues within the local community through cooperative governance, historic settlements can finance essential civic infrastructure while safeguarding resident wellbeing and long-term residential autonomy."'
)
text = text.replace(
    'ecological humility."',
    'ecological humility. Rediscovering the profound ecological and social wisdom of vernacular architecture offers contemporary society a visionary roadmap for constructing sustainable, climate-resilient human habitats grounded in aesthetic harmony and environmental humility."'
)

with open('tools/curriculum_batch_003/data_reading_c1_part3.py', 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated data_reading_c1_part3.py directly.')
