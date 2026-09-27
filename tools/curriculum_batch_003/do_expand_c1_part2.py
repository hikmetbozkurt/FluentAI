#!/usr/bin/env python3
"""
Applies paragraph expansions to data_reading_c1_part2.py to ensure all articles exceed 1000 words cleanly.
"""

import sys
sys.path.insert(0, 'tools/curriculum_batch_003')

with open('tools/curriculum_batch_003/data_reading_c1_part2.py', encoding='utf-8') as f:
    text = f.read()

# Article 5 expansions
text = text.replace(
    'prudent investment governance."',
    'prudent investment governance. This doctrinal fixation on immediate capital returns incentivized the systemic externalization of environmental hazards, effectively privatizing short-term corporate profits while socializing catastrophic ecological degradation onto future generations."'
)
text = text.replace(
    'prudent risk management."',
    'prudent risk management. Under this modernized legal doctrine, failure to account for quantifiable ecological variables constitutes a failure of analytical due diligence, exposing asset managers to regulatory censure and fiduciary liability for willful blindness to predictable economic disruptions."'
)
text = text.replace(
    'retirement savings."',
    'retirement savings. Consequently, mitigating global planetary warming ceases to be an ideological or ethical campaign; it emerges as an actuarial necessity to protect the fundamental capital base upon which long-term compound interest and societal prosperity depend."'
)
text = text.replace(
    'reputational ruin."',
    'reputational ruin. The judicial expansion of corporate climate litigation signifies a profound paradigm shift, transforming abstract environmental goals into concrete legal duties enforced through corporate governance structures and judicial restitution."'
)
text = text.replace(
    'direct shareholder revolt."',
    'direct shareholder revolt. By maintaining continuous board oversight and sponsoring binding shareholder resolutions, proactive asset managers convert passive financial holdings into transformative levers of industrial decarbonization and organizational accountability."'
)
text = text.replace(
    'for future generations."',
    'for future generations. The transition toward sustainable fiduciary jurisprudence affirms that economic profitability cannot be permanently divorced from ecological viability, establishing a mature foundation for durable wealth creation in an interconnected global economy."'
)

# Article 6 expansions
text = text.replace(
    'and environmental stressors."',
    'and environmental stressors. Hyper-efficient operational models operate on the fragile assumption of structural continuity, leaving enterprises dangerously unprepared when macro volatility shatters underlying logistical and macroeconomic assumptions."'
)
text = text.replace(
    'aggressive market expansion."',
    'aggressive market expansion. Cultivating intentional operational slack empowers organizations to absorb unexpected systemic shocks with minimal friction, transforming volatility from an existential crisis into an operational springboard for competitive superiority."'
)
text = text.replace(
    'responsive sensing antenna."',
    'responsive sensing antenna. This radical distribution of operational authority transforms frontline teams into autonomous tactical sensors capable of identifying emergent risks and executing localized adaptations long before centralized bureaucracies can formulate a response."'
)
text = text.replace(
    'catastrophic collapses."',
    'catastrophic collapses. When institutional leadership cultivates psychological safety, operational vulnerabilities are exposed and remediated collaboratively, systematically immunizing the organization against catastrophic blind spots and latent systemic failure."'
)
text = text.replace(
    'capturing positive volatility."',
    'capturing positive volatility. By capping exploratory downside while retaining uncapped upside potential, the barbell strategy allows enterprises to navigate macro volatility with serene structural confidence, transforming uncertainty into an engine of continuous innovation."'
)
text = text.replace(
    'reinvent the future amidst uncertainty."',
    'reinvent the future amidst uncertainty. Ultimately, institutional vitality is sustained not through rigid control or bureaucratic insulation, but through the deliberate design of dynamic learning mechanisms that turn environmental turbulence into organizational renewal."'
)

# Article 7 expansions
text = text.replace(
    'is fundamentally broken."',
    'is fundamentally broken. This algorithmic entrenchment of partisan animosity transforms democratic debate from a collective search for civic solutions into a zero-sum war of cultural attrition that paralyzes representative institutions."'
)
text = text.replace(
    'public-spirited conclusions."',
    'public-spirited conclusions. The transformative power of deliberative mini-publics lies in their capacity to insulate citizens from partisan commercial pressures, providing an egalitarian sanctuary where reasoned argument and epistemic curiosity supersede ideological tribalism."'
)
text = text.replace(
    'entrenched cultural chasms."',
    'entrenched cultural chasms. Experiencing the lived vulnerabilities of fellow citizens breaks the seductive spell of abstract ideological dogmatism, cultivating deep affective empathy and laying the psychosocial groundwork for durable democratic solidarity."'
)
text = text.replace(
    'mutually acceptable principles."',
    'mutually acceptable principles. Authentic persuasion requires relinquishing performative moral superiority in favor of reciprocal vulnerability, demonstrating that mutual respect and principled compromise remain the foundational virtues of self-governing republics."'
)
text = text.replace(
    'empathetic problem-solving."',
    'empathetic problem-solving. This profound depolarization demonstrates that democratic hostility is not an inevitable human condition, but a byproduct of flawed institutional architectures that can be actively repaired through deliberative innovation."'
)
text = text.replace(
    'twenty-first century."',
    'twenty-first century. By elevating deliberative assemblies to permanent constitutional partners, contemporary democracies can bridge elite technocracy and populist frustration, revitalizing civic faith in the enduring promise of democratic self-determination."'
)

# Article 8 expansions
text = text.replace(
    'multigenerational living arrangements."',
    'multigenerational living arrangements. The collapse of affordable single-family housing models has exposed the fragility of the isolated nuclear unit, prompting families to rediscover the collective economic strength embedded within traditional kinship networks."'
)
text = text.replace(
    'isolated nuclear households."',
    'isolated nuclear households. This dynamic intergenerational wealth buffering shields younger generations from predatory consumer debt while affording elderly family members dignified companionship and protection against inflationary asset erosion."'
)
text = text.replace(
    'market commodities cannot replicate."',
    'market commodities cannot replicate. In an era of escalating care commodification, the restoration of domestic caregiving reciprocity humanizes the household economy, anchoring family welfare in durable bonds of intergenerational affection and mutual obligation."'
)
text = text.replace(
    'domestic responsibilities."',
    'domestic responsibilities. Constructing explicit communication protocols and honoring physical boundaries prevents domestic enmeshment, allowing multigenerational families to enjoy collective economic solidarity without compromising personal autonomy and individual dignity."'
)
text = text.replace(
    'preserving residential autonomy."',
    'preserving residential autonomy. Urban environments that embrace flexible zoning and universal modular design provide the physical scaffolding required for extended families to thrive, fostering resilient, age-integrated communities across generations."'
)
text = text.replace(
    'intergenerational love."',
    'intergenerational love. Reconnecting the generations within the domestic sphere provides children with living anchors to their cultural ancestry, fostering an enduring emotional resilience that withstands the isolating pressures of modern hyper-individualism."'
)

with open('tools/curriculum_batch_003/data_reading_c1_part2.py', 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated data_reading_c1_part2.py directly.')
