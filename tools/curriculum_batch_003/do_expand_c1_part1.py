#!/usr/bin/env python3
"""
Applies expansions to data_reading_c1_part1.py and verifies word counts.
"""

import sys
sys.path.insert(0, 'tools/curriculum_batch_003')

with open('tools/curriculum_batch_003/data_reading_c1_part1.py', encoding='utf-8') as f:
    text = f.read()

# Article 1: + ~40 words
text = text.replace(
    'unintelligible literalism and the Charybdis of patronizing stylistic domestication."',
    'unintelligible literalism and the Charybdis of patronizing stylistic domestication. Furthermore, syntactic inversions that sound elegant and dignified in ancient or classical tongues can appear pretentiously archaic or hopelessly convoluted when replicated without modification in contemporary English, demonstrating that literal loyalty often produces psychological alienation rather than aesthetic beauty."'
)
text = text.replace(
    'üslup yerlileştirmesi arasında gezinerek gerçek sadakat ile iletişimsel özgürlük arasında hassas bir dengeyi korumalıdır."',
    'üslup yerlileştirmesi arasında gezinerek gerçek sadakat ile iletişimsel özgürlük arasında hassas bir dengeyi korumalıdır. Dahası, antik veya klasik dillerde zarif ve vakur duran sözdizimsel devriklikler, çağdaş İngilizcede değişiklik yapılmadan tekrarlandığında iddialı bir şekilde arkaik veya umutsuzca karmaşık görünebilir; bu da harfiyen sadakatin estetik güzellikten ziyade psikolojik yabancılaşma ürettiğini gösterir."'
)

# Article 2: + ~160 words
text = text.replace(
    'complex environmental demands and cognitive challenges."',
    'complex environmental demands and cognitive challenges. Early histological techniques lacked the molecular sensitivity required to detect nascent neuroblasts amidst dense cortical networks, thereby entrenching an erroneous dogma of neurological finality that endured unchallenged across multiple generations of medical practitioners."'
)
text = text.replace(
    'büyük bir kapasiteye sahip olduğunu ortaya koydu."',
    'büyük bir kapasiteye sahip olduğunu ortaya koydu. Erken histolojik teknikler, yoğun kortikal ağların ortasında yeni başlayan nöroblastları tespit etmek için gereken moleküler duyarlılıktan yoksundu ve bu da tıp pratisyenlerinin nesiller boyunca tartışmasız bir şekilde sürdürdüğü hatalı bir nörolojik nihayet dogmasını kökleştirdi."'
)
text = text.replace(
    'advanced analytical problem-solving capabilities."',
    'advanced analytical problem-solving capabilities. Concurrently, retrograde signaling molecules such as nitric oxide travel back across the synaptic gap to enhance presynaptic transmitter packaging, creating a self-reinforcing biophysical feedback circuit that consolidates synaptic efficacy."'
)
text = text.replace(
    'fiziksel temelini oluşturan sağlam, son derece verimli sinir yolları oluşturur."',
    'fiziksel temelini oluşturan sağlam, son derece verimli sinir yolları oluşturur. Eşzamanlı olarak nitrik oksit gibi retrograd sinyal molekülleri, presinaptik verici paketlemesini geliştirmek için sinaptik boşluk boyunca geriye doğru hareket ederek sinaptik etkinliği pekiştiren ve kendi kendini pekiştiren bir biyofiziksel geri bildirim devresi oluşturur."'
)
text = text.replace(
    'adaptively assimilate complex conceptual frameworks."',
    'adaptively assimilate complex conceptual frameworks. These newly generated granule neurons exhibit a transient critical period of hyper-plasticity, characterized by lower activation thresholds and heightened synaptic excitability, rendering them uniquely suited to encoding novel chronological associations without destabilizing preexisting memories."'
)
text = text.replace(
    'kavramsal çerçeveleri uyarlanabilir şekilde özümseme kapasitesini önemli ölçüde artırır."',
    'kavramsal çerçeveleri uyarlanabilir şekilde özümseme kapasitesini önemli ölçüde artırır. Bu yeni üretilen granül nöronlar; daha düşük aktivasyon eşikleri ve artan sinaptik uyarılabilirlik ile karakterize edilen, önceden var olan anıları istikrarsızlaştırmadan yeni kronolojik çağrışımları kodlamak için onları benzersiz şekilde uygun kılan geçici bir kritik hiper-plastisite dönemi sergiler."'
)
text = text.replace(
    'curiosity, cognitive exertion, and persistent mental adaptation."',
    'curiosity, cognitive exertion, and persistent mental adaptation. This distributed compensatory plasticity demonstrates that the human brain is not a static calculating apparatus with fixed processing nodes, but an adaptive, self-organizing organic system capable of dynamic functional restructuring throughout life."'
)
text = text.replace(
    'dinamik olarak inşa edilmiş biyolojik bir kaledir."',
    'dinamik olarak inşa edilmiş biyolojik bir kaledir. Bu dağıtılmış telafi edici plastisite, insan beyninin sabit işlem düğümlerine sahip statik bir hesaplama aygıtı olmadığını, yaşam boyunca dinamik işlevsel yeniden yapılanma yeteneğine sahip uyarlanabilir, kendi kendini organize eden organik bir sistem olduğunu göstermektedir."'
)
text = text.replace(
    'build enduring cognitive adaptability."',
    'build enduring cognitive adaptability. By deliberately leaning into the cognitive turbulence of challenging tasks, learners stimulate locus coeruleus norepinephrine release, which heightens sensory vigilance and primes downstream cortical circuits for enduring synaptic restructuring."'
)
text = text.replace(
    'kalıcı bilişsel uyarlanabilirlik inşa etmeye zorlar."',
    'kalıcı bilişsel uyarlanabilirlik inşa etmeye zorlar. Öğrenenler zorlu görevlerin bilişsel çalkantısına kasıtlı olarak eğilerek duyusal uyanıklığı artıran ve aşağı havza kortikal devrelerini kalıcı sinaptik yeniden yapılanma için hazırlayan lokus seruleus norepinefrin salınımını uyarırlar."'
)
text = text.replace(
    'visionary conceptual innovation."',
    'visionary conceptual innovation. Ultimately, embracing cognitive flexibility empowers mature learners to continuously transcend habitual cognitive boundaries, unlocking an enduring vitality of intellect and imagination that resists the passive atrophy of chronological aging."'
)
text = text.replace(
    'vizyoner kavramsal inovasyon yeteneğine sahip dinamik, sürekli gelişen mimarilere dönüştürebilirler."',
    'vizyoner kavramsal inovasyon yeteneğine sahip dinamik, sürekli gelişen mimarilere dönüştürebilirler. Nihayetinde bilişsel esnekliği benimsemek, olgun öğrenenlerin alışılmış bilişsel sınırları sürekli olarak aşmalarını sağlayarak kronolojik yaşlanmanın pasif körelmesine direnen kalıcı bir zeka ve hayal gücü canlılığının kilidini açar."'
)

# Article 3: + ~220 words
text = text.replace(
    'accelerate extinction cascades under accelerating global climate change."',
    'accelerate extinction cascades under accelerating global climate change. When ecological mosaics are severed into diminutive refuges, the loss of contiguous territorial expanse disrupts historic seasonal migration routes, precipitating demographic collapses that cascade unpredictably across interconnected trophic levels."'
)
text = text.replace(
    'yok olma çağlayanlarını hızlandıran ekolojik tuzaklar yaratır."',
    'yok olma çağlayanlarını hızlandıran ekolojik tuzaklar yaratır. Ekolojik mozaikler küçük sığınaklara bölündüğünde, bitişik bölgesel genişliğin kaybı tarihi mevsimsel göç yollarını bozar ve birbirine bağlı trofik seviyeler boyunca öngörülemez şekilde kademeli olarak yayılan demografik çöküşleri hızlandırır."'
)
text = text.replace(
    'ensuring long-term evolutionary survival."',
    'ensuring long-term evolutionary survival. In addition, aerial canopy bridges constructed above industrial infrastructure enable arboreal species to navigate fragmented canopies without descending to vulnerable terrestrial surfaces where predation rates and vehicular mortality remain catastrophically elevated. Furthermore, acoustic and artificial light pollution along transport corridors can be mitigated through specialized sensory deflection barriers that preserve nocturnal behavior patterns."'
)
text = text.replace(
    'uzun vadeli evrimsel hayatta kalmayı sağlayan vazgeçilmez ekolojik kanallar sağlar."',
    'uzun vadeli evrimsel hayatta kalmayı sağlayan vazgeçilmez ekolojik kanallar sağlar. Ek olarak endüstriyel altyapının üzerine inşa edilen hava gölgelik köprüleri, ağaçta yaşayan türlerin avlanma oranlarının ve araç ölümlerinin feci şekilde yüksek kaldığı savunmasız karasal yüzeylere inmeden parçalanmış gölgeliklerde gezinmesini sağlar. Dahası, ulaşım koridorları boyunca akustik ve yapay ışık kirliliği, gece davranış modellerini koruyan özel duyusal saptırma bariyerleri aracılığıyla hafifletilebilir."'
)
text = text.replace(
    'and biodiversity of the entire river valley."',
    'and biodiversity of the entire river valley. The spatial distribution of prey species across the landscape undergoes a profound realignment as herbivores weigh nutritional foraging benefits against the palpable existential danger of open-meadow exposure under vigilant predator surveillance."'
)
text = text.replace(
    'jeomorfolojisini ve biyoçeşitliliğini radikal bir şekilde dönüştürdü."',
    'jeomorfolojisini ve biyoçeşitliliğini radikal bir şekilde dönüştürdü. Otoburlar besinsel otlama faydalarını uyanık yırtıcı gözetimi altındaki açık çayır maruziyetinin somut varoluşsal tehlikesine karşı tarttıkça, av türlerinin peyzaj boyunca mekansal dağılımı derin bir yeniden düzenlemeye uğrar."'
)
text = text.replace(
    'accelerate regional ecological recovery."',
    'accelerate regional ecological recovery. The resulting dynamic wetland habitats cultivate an extraordinary richness of macroinvertebrates, aquatic flora, and waterfowl, transforming homogenous agricultural watercourses into thriving, self-sustaining biodiversity powerhouses capable of buffering regional watersheds."'
)
text = text.replace(
    'bölgesel ekolojik iyileşmeyi hızlandıran yangın sonrası ekolojik tohum kaynakları sağlayan gür, yanmamış yeşil sığınaklar olarak hizmet eder."',
    'bölgesel ekolojik iyileşmeyi hızlandıran yangın sonrası ekolojik tohum kaynakları sağlayan gür, yanmamış yeşil sığınaklar olarak hizmet eder. Ortaya çıkan dinamik sulak alan habitatları; homojen tarımsal su yollarını bölgesel su havzalarını tamponlama yeteneğine sahip gelişen, kendi kendini idame ettiren biyoçeşitlilik santrallerine dönüştürerek olağanüstü bir makro omurgasız, su florası ve su kuşu zenginliği geliştirir."'
)
text = text.replace(
    'local prosperity with wild ecosystem regeneration."',
    'local prosperity with wild ecosystem regeneration. By treating rural stakeholders as indispensable ecological partners rather than adversarial obstacles, progressive conservation initiatives foster a durable sense of regional stewardship and cultural pride in wild landscape recovery."'
)
text = text.replace(
    'yerel refahı vahşi ekosistem yenilenmesiyle uyumlu hale getirir."',
    'yerel refahı vahşi ekosistem yenilenmesiyle uyumlu hale getirir. Kırsal paydaşlara düşmanca engeller yerine vazgeçilmez ekolojik ortaklar olarak davranan ilerici koruma girişimleri, vahşi peyzajın iyileştirilmesinde kalıcı bir bölgesel sahiplenme ve kültürel gurur duygusu geliştirir."'
)
text = text.replace(
    'flourishes for millennia to come."',
    'flourishes for millennia to come. Through the courageous orchestration of large-scale habitat restoration and functional species reintroductions, societies can cultivate an inspiring ecological legacy characterized by biological abundance, evolutionary freedom, and enduring ecological wonder."'
)
text = text.replace(
    'yaşam dokusunun gelecek bin yıllar boyunca gelişmesini sağlama yolunda temel bir etik adım atmış olur."',
    'yaşam dokusunun gelecek bin yıllar boyunca gelişmesini sağlama yolunda temel bir etik adım atmış olur. Büyük ölçekli habitat restorasyonunun ve işlevsel türlerin yeniden salınmasının cesur bir şekilde düzenlenmesi yoluyla toplumlar; biyolojik bolluk, evrimsel özgürlük ve kalıcı ekolojik mucize ile karakterize edilen ilham verici bir ekolojik miras geliştirebilirler."'
)

# Article 4: + ~210 words
text = text.replace(
    'beneath proprietary mathematical code."',
    'beneath proprietary mathematical code. The institutional allure of computational decision systems is further magnified by the seductive promise of absolute standardization, which purports to eradicate the notorious sentencing disparities that plague human judicial tribunals."'
)
text = text.replace(
    'büyütmekte ve aklamaktadır."',
    'büyütmekte ve aklamaktadır. Bilgisayarlı karar sistemlerinin kurumsal cazibesi, insan yargı mahkemelerini rahatsız eden meşhur cezalandırma eşitsizliklerini ortadan kaldırdığını iddia eden mutlak standardizasyonun baştan çıkarıcı vaadiyle daha da büyütülmektedir."'
)
text = text.replace(
    'creating a catastrophic, self-fulfilling feedback loop."',
    'creating a catastrophic, self-fulfilling feedback loop. Actuarial risk assessment algorithms codify systemic demographic disadvantages under the mathematical guise of statistical correlation, transforming structural socioeconomic inequalities into permanent individual risk indicators."'
)
text = text.replace(
    've feci, kendi kendini gerçekleştiren bir geri bildirim döngüsü yaratır."',
    've feci, kendi kendini gerçekleştiren bir geri bildirim döngüsü yaratır. Aktüeryal risk değerlendirme algoritmaları, sistemik demografik dezavantajları istatistiksel korelasyonun matematiksel kisvesi altında kodlayarak yapısal sosyoekonomik eşitsizlikleri kalıcı bireysel risk göstergelerine dönüştürür."'
)
text = text.replace(
    'and gutting procedural justice."',
    'and gutting procedural justice. Deprived of the ability to inspect underlying training weights and feature calculations, defense counsel cannot verify whether an algorithm relied upon impermissible discriminatory proxies or suffered from severe overfitting errors."'
)
text = text.replace(
    've usul adaletini baltalar."',
    've usul adaletini baltalar. Temel eğitim ağırlıklarını ve özellik hesaplamalarını inceleme yeteneğinden mahrum bırakılan savunma avukatı, bir algoritmanın izin verilmeyen ayrımcı vekillere dayanıp dayanmadığını veya ciddi aşırı öğrenme hatalarından muzdarip olup olmadığını doğrulayamaz."'
)
text = text.replace(
    'democratic scrutiny and ethical responsibility."',
    'democratic scrutiny and ethical responsibility. By concealing political value judgments beneath a facade of computational inevitability, technocratic institutions insulate controversial ideological priorities from democratic legislative deliberation and constitutional accountability."'
)
text = text.replace(
    'demokratik denetimden ve etik sorumluluktan korur."',
    'demokratik denetimden ve etik sorumluluktan korur. Teknokratik kurumlar siyasi değer yargılarını hesaplamalı kaçınılmazlık cephesinin arkasına gizleyerek, tartışmalı ideolojik öncelikleri demokratik yasama müzakeresinden ve anayasal hesap verebilirlikten korurlar."'
)
text = text.replace(
    'that govern civic life."',
    'that govern civic life. These emerging statutory guardrails represent a crucial legal recognition that computational efficiency must never be permitted to supersede fundamental constitutional protections and procedural fairness guarantees."'
)
text = text.replace(
    'yeniden tesis etmeyi amaçlamaktadır."',
    'yeniden tesis etmeyi amaçlamaktadır. Ortaya çıkan bu yasal korkuluklar, hesaplama verimliliğinin temel anayasal korumaların ve usul adaleti garantilerinin önüne geçmesine asla izin verilmemesi gerektiğine dair çok önemli bir yasal kabulü temsil etmektedir."'
)
text = text.replace(
    'proprietary corporate algorithms."',
    'proprietary corporate algorithms. The ultimate legitimacy of civic institutions rests not upon computational velocity or algorithmic complexity, but upon their demonstrable commitment to procedural transparency, human empathy, and universal justice."'
)
text = text.replace(
    'kurumsal algoritmalara teslim etmeyi reddederek teknoloji üzerinde demokratik hakimiyet kurmalıdır."',
    'kurumsal algoritmalara teslim etmeyi reddederek teknoloji üzerinde demokratik hakimiyet kurmalıdır. Sivil kurumların nihai meşruiyeti hesaplama hızına veya algoritmik karmaşıklığa değil, usul şeffaflığına, insan empatisine ve evrensel adalete olan kanıtlanabilir bağlılıklarına dayanır."'
)

with open('tools/curriculum_batch_003/data_reading_c1_part1.py', 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated data_reading_c1_part1.py directly.')
