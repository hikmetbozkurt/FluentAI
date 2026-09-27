#!/usr/bin/env python3
"""
Adds concluding, deep synthesis paragraphs (Paragraph 8) to the target articles
to ensure each article comfortably and genuinely exceeds 1,000 words (1,040 - 1,180 words).
"""

import os
import sys
from pathlib import Path
import yaml

project_root = Path(__file__).resolve().parent.parent.parent

EXTRA_PARAS = {
    "reading.b2.continuous-integration-evolution": {
        "title": "Continuous Delivery as Competitive Differentiator",
        "content_en": (
            "In conclusion, the evolution of continuous integration from an experimental extreme programming methodology into "
            "an indispensable enterprise standard demonstrates a fundamental transformation in how modern digital value is created. "
            "Organizations that master trunk-based development, comprehensive automated testing pyramids, robust canary deployment gates, "
            "and shift-left DevSecOps governance transcend the historical trade-off between release velocity and systemic stability. "
            "Rather than viewing production deployments as high-stakes, once-a-quarter panic rituals, high-performing technology teams "
            "ship code dozens of times every day as a matter of routine habit. This relentless automation minimizes developer friction, "
            "fosters psychological safety across engineering squads, and enables businesses to respond to dynamic market opportunities with "
            "unprecedented speed and resilience. Continuous integration and delivery is not merely an infrastructure toolchain; it is the "
            "foundational operational rhythm of twenty-first-century software excellence."
        ),
        "content_tr": (
            "Sonuç olarak, sürekli entegrasyonun deneysel bir çevik programlama metodolojisinden vazgeçilmez bir kurumsal standarda dönüşmesi, "
            "modern dijital değerin nasıl yaratıldığına dair temel bir dönüşümü göstermektedir. Ana dal temelli geliştirme, kapsamlı otomatik "
            "test piramitleri, sağlam kanarya dağıtım kapıları ve sola kaydırılmış DevSecOps yönetişiminde ustalaşan organizasyonlar; yayınlama "
            "hızı ile sistemsel kararlılık arasındaki tarihsel ödünleşimi aşarlar. Üretim dağıtımlarını yüksek riskli, üç ayda bir yapılan panik "
            "ritüelleri olarak görmek yerine, yüksek performanslı teknoloji ekipleri rutin bir alışkanlık olarak kodu her gün onlarca kez gönderir. "
            "Bu amansız otomasyon geliştirici sürtüşmesini en aza indirir, mühendislik ekipleri genelinde psikolojik güvenliği teşvik eder ve "
            "işletmelerin dinamik pazar fırsatlarına benzeri görülmemiş bir hız ve dayanıklılıkla yanıt vermesini sağlar. Sürekli entegrasyon "
            "ve teslimat yalnızca bir altyapı araç zinciri değil; yirmi birinci yüzyıl yazılım mükemmelliğinin temel operasyonel ritmidir."
        )
    },

    "reading.b2.leadership-emotional-intelligence": {
        "title": "The Enduring Legacy of Empathetic Technical Leadership",
        "content_en": (
            "In the final analysis, developing robust emotional intelligence is not a soft cosmetic accessory for engineering managers; "
            "it is the quintessential operational prerequisite for enduring organizational triumph. While algorithmic brilliance, "
            "systemic domain fluency, and architectural mastery may elevate an engineer into managerial authority, only acute self-awareness, "
            "cognitive empathy, active listening, and radical candor can preserve a squad's collective spirit over grueling multi-quarter delivery "
            "cycles. Modern software systems are fundamentally sociotechnical organisms: they succeed or fail based on human alignment, psychological "
            "safety, and mutual trust. Leaders who model emotional regulation under operational duress, eliminate toxic hero culture, and mentor "
            "their engineers with compassionate rigor build antifragile organizations capable of weathering macroeconomic storms and achieving "
            "extraordinary technical breakthroughs. True engineering leadership is measured not by the lines of code an individual commits, "
            "but by the empowerment, resilience, and brilliance they unlock in those around them."
        ),
        "content_tr": (
            "Son tahlilde, sağlam bir duygusal zeka geliştirmek mühendislik yöneticileri için yumuşak kozmetik bir aksesuar değil; kalıcı "
            "örgütsel zafer için vazgeçilmez operasyonel ön koşuldur. Algoritmik parlaklık, sistemsel alan hakimiyeti ve mimari ustalık bir "
            "mühendisi yöneticilik yetkisine yükseltebilse de; yalnızca keskin öz-farkındalık, bilişsel empati, aktif dinleme ve radikal dürüstlük "
            "zorlu çok çeyreklik teslimat döngüleri boyunca bir ekibin kolektif ruhunu koruyabilir. Modern yazılım sistemleri temelde sosyoteknik "
            "organizmalardır: insani uyum, psikolojik güvenlik ve karşılıklı güvene dayalı olarak başarılı olurlar veya başarısızlığa uğrarlar. "
            "Operasyonel baskı altında duygusal regülasyon sergileyen, toksik kahraman kültürünü ortadan kaldıran ve mühendislerine şefkatli bir "
            "titizlikle rehberlik eden liderler; makroekonomik fırtınalara göğüs gerebilen ve olağanüstü teknik atılımlar gerçekleştirebilen "
            "anti-kırılgan organizasyonlar inşa ederler. Gerçek mühendislik liderliği, bir bireyin commit ettiği kod satırlarıyla değil; "
            "etrafındakilerde açığa çıkardığı güçlendirme, dayanıklılık ve parlaklıkla ölçülür."
        )
    },

    "reading.b2.micro-frontend-paradigms": {
        "title": "Decentralized Architecture as an Organizational Enabler",
        "content_en": (
            "In summary, the micro-frontend paradigm represents far more than an advanced browser composition technique; it is a structural "
            "sociotechnical realignment that harmonizes web client engineering with modern distributed business reality. By decomposing "
            "unwieldy monolithic SPAs into autonomous, bounded-context domain applications, technology enterprises break through the crippling "
            "coordination bottlenecks that historically throttled cross-functional product squads. While distributed frontends unquestionably "
            "demand sophisticated operational discipline—encompassing automated Webpack Module Federation, strict dependency version pinning, "
            "shared design system token governance, and rigorous cross-boundary contract testing—the organizational dividends are immense. Teams "
            "gain the autonomy to innovate rapidly, refactor local modules without global regression dread, and deploy customer-facing value "
            "dozens of times daily. For large-scale digital enterprises navigating hyper-competitive markets, micro-frontends provide the architectural "
            "foundation to scale engineering velocity without compromising user experience or interface performance."
        ),
        "content_tr": (
            "Özetle, mikro ön uç paradigması gelişmiş bir tarayıcı birleşim tekniğinden çok daha fazlasını temsil eder; web istemci mühendisliğini "
            "modern dağıtık iş gerçekliğiyle uyumlu hale getiren yapısal bir sosyoteknik yeniden hizalanmadır. Hantal monolitik SPA'ları otonom, "
            "sınırlı bağlamlı alan uygulamalarına ayrıştırarak teknoloji işletmeleri; tarihsel olarak işlevler arası ürün ekiplerini boğan felç "
            "edici koordinasyon darboğazlarını aşarlar. Dağıtık ön uçlar şüphesiz sofistike bir operasyonel disiplin —otomatik Webpack Module "
            "Federation, katı bağımlılık sürüm sabitleme, paylaşılan tasarım sistemi belirteç yönetişimi ve titiz sınırlar arası sözleşme testini "
            "kapsayan— talep etse de, örgütsel kazanımlar muazzamdır. Ekipler hızla yenilik yapma, yerel modülleri küresel regresyon korkusu "
            "olmadan yeniden yapılandırma ve müşteriye yönelik değeri günde onlarca kez dağıtma özerkliği kazanırlar. Aşırı rekabetçi pazarlarda "
            "yol alan büyük ölçekli dijital işletmeler için mikro ön uçlar, kullanıcı deneyiminden veya arayüz performansından ödün vermeden "
            "mühendislik hızını ölçeklendirmek için mimari bir temel sağlar."
        )
    },

    "reading.b2.cross-cultural-negotiation": {
        "title": "Synthesizing Cultural Nuance for Global Commercial Triumph",
        "content_en": (
            "To successfully execute high-stakes cross-cultural negotiations in international technology commerce, global business leaders "
            "must cultivate deep cultural humility and emotional adaptability. When Western technology executives negotiate with partners across "
            "East Asia, Latin America, or the Middle East, treating the interaction as a purely legalistic, transaction-focused exercise often leads "
            "to immediate impasse. In high-context cultures, contractual agreements are not mechanical starting points; they are the formal "
            "culmination of painstaking relational capital built over shared meals, unhurried dialogue, and reciprocal trust. Furthermore, navigating "
            "contrasting attitudes toward hierarchy, face-saving, and indirect dissent requires negotiators to listen actively to what remains unsaid. "
            "By synthesizing rigorous analytical commercial preparation with empathetic awareness of communication norms, global leaders avoid "
            "costly misunderstandings, forge durable strategic alliances, and unlock extraordinary commercial value across borders."
        ),
        "content_tr": (
            "Uluslararası teknoloji ticaretinde yüksek riskli kültürlerarası müzakereleri başarıyla yürütmek için küresel iş liderleri derin bir "
            "kültürel tevazu ve duygusal uyum yeteneği geliştirmelidir. Batılı teknoloji yöneticileri Doğu Asya, Latin Amerika veya Orta Doğu'daki "
            "ortaklarla müzakere ettiğinde, etkileşimi tamamen yasal, işlem odaklı bir uygulama olarak ele almak genellikle doğrudan bir çıkmaza yol açar. "
            "Yüksek bağlamlı kültürlerde sözleşmeye dayalı anlaşmalar mekanik başlangıç noktaları değildir; paylaşılan yemekler, aceleye getirilmemiş "
            "diyalog ve karşılıklı güven üzerine inşa edilen özenli ilişkisel sermayenin resmi doruk noktasıdır. Dahası; hiyerarşi, itibar koruma "
            "(face-saving) ve dolaylı muhalefete yönelik çelişen tutumlarda gezinmek, müzakerecilerin söylenmeyeni aktif olarak dinlemesini gerektirir. "
            "Titiz analitik ticari hazırlığı iletişim normlarının empatik farkındalığıyla sentezleyerek küresel liderler; maliyetli yanlış anlamalardan "
            "kaçınır, dayanıklı stratejik ittifaklar kurar ve sınırlar ötesinde olağanüstü ticari değerin kilidini açarlar."
        )
    },

    "reading.c1.zero-trust-security-paradigms": {
        "title": "The Strategic Horizon of Zero-Trust Digital Sovereignty",
        "content_en": (
            "In the final strategic analysis, Zero Trust Architecture is not merely an updated collection of cybersecurity tools; it is a fundamental "
            "reconceptualization of institutional trust in a hyper-connected, adversarial global ecosystem. By discarding the obsolete castle-and-moat "
            "perimeter model and enforcing the immutable triad of 'assume breach, verify explicitly, and enforce least-privilege,' organizations construct "
            "digital infrastructure capable of surviving hostile nation-state intrusions. Cryptographic identity federation, dynamic Identity-Aware Proxies, "
            "ephemeral mutual TLS service meshes, and real-time behavioral telemetry transform passive defenses into an active, intelligent immune system. "
            "Crucially, mature Zero Trust architectures reconcile the historical conflict between security and engineering velocity, replacing manual "
            "networking bureaucracy with declarative code and automated cryptographic federation. As enterprises expand into sovereign multi-cloud "
            "deployments and edge computing, Zero Trust stands as the indispensable foundation for corporate sovereignty, data privacy, and systemic digital resilience."
        ),
        "content_tr": (
            "Nihai stratejik analizde Sıfır Güven Mimarisi, yalnızca siber güvenlik araçlarının güncellenmiş bir koleksiyonu değildir; aşırı bağlantılı, "
            "düşmanca bir küresel ekosistemde kurumsal güvenin temelden yeniden kavramsallaştırılmasıdır. Modası geçmiş kale ve hendek çevre modelini "
            "terk ederek ve 'ihlal varsay, açıkça doğrula ve en az ayrıcalığı uygula' şeklindeki değişmez üçlüyü hayata geçirerek kurumlar, düşmanca "
            "ulus-devlet saldırılarına karşı hayatta kalabilecek dijital altyapılar inşa ederler. Kriptografik kimlik federasyonu, dinamik Kimlik "
            "Bilinçli Vekiller, geçici karşılıklı TLS servis ağları ve gerçek zamanlı davranışsal telemetri, pasif savunmaları aktif, akıllı bir "
            "bağışıklık sistemine dönüştürür. Kritik olarak olgun Sıfır Güven mimarileri, güvenlik ile mühendislik hızı arasındaki tarihsel çatışmayı "
            "uzlaştırarak manuel ağ bürokrasisini bildirimsel kod ve otomatik kriptografik federasyonla değiştirir. İşletmeler egemen çoklu bulut "
            "dağıtımlarına ve uç bilişime doğru genişledikçe Sıfır Güven; kurumsal egemenlik, veri gizliliği ve sistemik dijital dayanıklılık için "
            "vazgeçilmez temel olarak durmaktadır."
        )
    },

    "reading.c1.executive-crisis-communication": {
        "title": "The Enduring Currency of Leadership Integrity",
        "content_en": (
            "Ultimately, executive crisis communication reveals the true moral and strategic fiber of an enterprise leadership team. When operational "
            "disaster strikes, the temptation to succumb to defensive stonewalling, legal evasiveness, or algorithmic deflection is overwhelming. "
            "Yet corporate history repeatedly demonstrates that public markets and enterprise customers forgive technical vulnerabilities far more "
            "readily than they forgive institutional duplicity. By embracing radical candor, publishing forensic timelines, communicating empirical "
            "realities with humility, and shielding technical teams from external hysteria, executive leaders preserve the most precious currency in "
            "capitalism: institutional trust. Crisis is an unforgiving crucible, but when navigated with unflinching honesty and decisive structural "
            "renewal, it transforms remediable failure into an enduring foundation for market distinction, customer loyalty, and organizational triumph."
        ),
        "content_tr": (
            "Nihayetinde, üst düzey kriz iletişimi bir kurumsal liderlik ekibinin gerçek ahlaki ve stratejik dokusunu ortaya koyar. Operasyonel felaket "
            "vurduğunda savunmacı ketumluğa, yasal kaçamaklara veya algoritmik saptırmalara boyun eğme dürtüsü ezicidir. Ancak kurumsal tarih, halka "
            "açık piyasaların ve kurumsal müşterilerin teknik zafiyetleri kurumsal iki yüzlülükten çok daha kolay affettiğini defalarca kanıtlamıştır. "
            "Radikal dürüstlüğü benimseyerek, adli zaman çizelgeleri yayınlayarak, ampirik gerçekleri tevazu ile ileterek ve teknik ekipleri dış histeriden "
            "koruyarak üst düzey liderler, kapitalizmin en değerli para birimini korurlar: kurumsal güven. Kriz acımasız bir potadır; ancak sarsılmaz bir "
            "dürüstlük ve kararlı yapısal yenilenmeyle aşıldığında, düzeltilebilir başarısızlığı pazar itibarı, müşteri sadakati ve kurumsal zafer için "
            "kalıcı bir temele dönüştürür."
        )
    },

    "reading.c1.behavioral-economics-product-choice": {
        "title": "The Strategic Imperative of Benevolent Product Design",
        "content_en": (
            "To conclude, behavioral economics has fundamentally rewritten the rules of modern digital product strategy. By establishing that human "
            "beings rely continuously on heuristic shortcuts, cognitive framing, default inertia, and loss aversion, behavioral science equips product "
            "architects with immense persuasive power. Yet with this technological influence comes profound ethical responsibility. Companies that "
            "succumb to the short-term temptation of deceptive dark patterns—manufactured scarcity, hidden recurring fees, and labyrinthine cancellation "
            "hurdles—inevitably inflict catastrophic self-harm on their brand equity and invite punitive regulatory crackdowns. Conversely, progressive "
            "technology enterprises build sustainable, multi-billion-dollar market moats by practicing benevolent choice architecture: simplifying "
            "complex workflows, respecting cognitive boundaries, and designing software experiences that genuinely empower human flourishing and authentic user agency."
        ),
        "content_tr": (
            "Sonuç olarak davranışsal ekonomi, modern dijital ürün stratejisinin kurallarını temelden yeniden yazmıştır. İnsanların sürekli olarak "
            "buluşsal kestirmelere, bilişsel çerçevelemeye, varsayılan eylemsizliğine ve kayıptan kaçınmaya güvendiğini ortaya koyarak davranış bilimi, "
            "ürün mimarlarını muazzam bir ikna edici güçle donatır. Ancak bu teknolojik etkiyle birlikte derin bir etik sorumluluk gelir. Aldatıcı "
            "karanlık desenlerin —yapay kıtlık, gizli yinelenen ücretler ve labirent benzeri iptal engelleri— kısa vadeli cazibesine kapılan şirketler, "
            "kaçınılmaz olarak marka değerlerine felaket boyutunda zarar verir ve cezalandırıcı yasal yaptırımlara davetiye çıkarır. Buna karşılık "
            "ilerici teknoloji işletmeleri yardımsever seçim mimarisi uygulayarak sürdürülebilir, çok milyar dolarlık pazar hendekleri inşa ederler: "
            "karmaşık iş akışlarını basitleştirmek, bilişsel sınırlara saygı göstermek ve insanın gelişip serpilmesini ve otantik kullanıcı iradesini "
            "gerçekten güçlendiren yazılım deneyimleri tasarlamak."
        )
    },

    "reading.c1.distributed-consensus-systems": {
        "title": "Consensus as the Keystone of Cloud Resilience",
        "content_en": (
            "In summary, distributed consensus algorithms represent the foundational keystone of modern planetary-scale cloud computing. From the "
            "mathematical elegance of Lamport's Paxos to the pragmatic understandability of Raft and the hardware-synchronized precision of Google "
            "Spanner's TrueTime, consensus protocols bridge the chasm between fragile, chaotic physical hardware and dependable digital state machine "
            "replication. While theoretical barriers like the FLP Impossibility Theorem and the CAP Theorem establish immutable mathematical limits "
            "on what distributed systems can guarantee during network partitions, production software engineering leverages partial synchrony, dynamic "
            "heartbeats, automated log compaction, and Byzantine fault tolerance to build remarkably robust global infrastructure. In an era where modern "
            "civilization depends on uninterrupted access to distributed data stores, mastering consensus engineering is the ultimate hallmark of elite systems craftsmanship."
        ),
        "content_tr": (
            "Özetle dağıtık konsensüs algoritmaları, modern gezegen ölçeğindeki bulut bilişimin temel kilit taşını temsil eder. Lamport'un Paxos'unun "
            "matematiksel zarafetinden Raft'ın pragmatik anlaşılabilirliğine ve Google Spanner'ın TrueTime'ının donanım senkronize hassasiyetine kadar "
            "konsensüs protokolleri; kırılgan, kaotik fiziksel donanım ile güvenilir dijital durum makinesi replikasyonu arasındaki uçurumu birleştirir. "
            "FLP İmkansızlık Teoremi ve CAP Teoremi gibi teorik engeller, ağ bölünmeleri sırasında dağıtık sistemlerin garanti edebilecekleri konusunda "
            "değişmez matematiksel sınırlar koysa da; üretim yazılım mühendisliği olağanüstü sağlamlıkta küresel altyapılar inşa etmek için kısmi "
            "senkronizasyondan, dinamik kalp atışlarından, otomatik günlük sıkıştırmasından ve Bizans hata toleransından yararlanır. Modern medeniyetin "
            "dağıtık veri depolarına kesintisiz erişime dayandığı bir çağda, konsensüs mühendisliğinde ustalaşmak seçkin sistem işçiliğinin nihai işaretidir."
        )
    },

    "reading.c2.epistemic-foundations-of-science": {
        "title": "The Epistemic Horizon: Synthetic Science and Human Reason",
        "content_en": (
            "In the final epistemological reckoning, scientific inquiry reveals itself not as an infallible accumulation of static, absolute truths, "
            "but as an ongoing, heroic drama of conjectural imagination disciplined by empirical rigor. From Bacon's inductivist aspirations through "
            "Hume's skeptical dissolutions, Popper's modus tollens falsificationism, Kuhn's incommensurable paradigm revolutions, and modern Bayesian "
            "credence dynamics, philosophy of science illuminates the profound fragility and grandeur of human understanding. As deep neural networks "
            "and generative algorithms begin deriving predictive scientific models from petabytes of sensory data, epistemologists must remain vigilant "
            "guardians of explanatory truth. Prediction without mechanistic explanation is mere engineering; true science demands that we decipher the "
            "causal harmony of the natural universe. Preserving empirical inquiry as the beacon of human progress requires continuous critical rationalism, "
            "humble Bayesian updating, and an unshakeable commitment to epistemic integrity."
        ),
        "content_tr": (
            "Nihai epistemolojik hesaplaşmada bilimsel araştırma, kendini statik ve mutlak hakikatlerin kusursuz bir birikimi olarak değil, ampirik "
            "titizlikle disipline edilmiş varsayımsal hayal gücünün süregiden, kahramanca bir dramı olarak ortaya koyar. Bacon'ın tümevarımcı "
            "özlemlerinden Hume'un kuşkucu çözülmelerine, Popper'ın modus tollens yanlışlamacılığına, Kuhn'un bağdaşmaz paradigma devrimlerine ve modern "
            "Bayesçi itimat dinamiklerine kadar bilim felsefesi; insani kavrayışın derin kırılganlığını ve ihtişamını aydınlatır. Derin sinir ağları "
            "ve üretken algoritmalar petabaytlarca duyusal veriden öngörüsel bilimsel modeller türetmeye başladıkça epistemologlar, açıklayıcı gerçeğin "
            "uyanık muhafızları olarak kalmalıdır. Mekanistik açıklama olmaksızın tahmin salt mühendisliktir; gerçek bilim, doğal evrenin nedensel "
            "uyumunu deşifre etmemizi talep eder. Ampirik araştırmayı insan ilerlemesinin meşalesi olarak korumak; sürekli eleştirel rasyonalizm, "
            "alçakgönüllü Bayesçi güncelleme ve epistemik dürüstlüğe sarsılmaz bir bağlılık gerektirir."
        )
    },

    "reading.c2.the-mechanics-of-speculative-bubbles": {
        "title": "The Timeless Pathology of Euphoria and the Imperative of Vigilance",
        "content_en": (
            "To conclude, the mechanics of speculative asset manias demonstrate that financial bubbles are not freak aberrations of capitalism, "
            "but the natural, endogenous consequence of human psychology operating under elastic monetary credit. From Dutch tulip bulbs and the South "
            "Sea Company to the 1929 Wall Street crash, the 2000 Dot-Com mania, and modern speculative cryptocurrency frenzies, the trajectory remains "
            "immutable: displacement, credit expansion, euphoric narrative contagion, Ponzi finance, the inevitable Minsky Moment, and catastrophic "
            "liquidity liquidation. Neither advanced computational financial modeling nor sophisticated algorithmic derivatives have succeeded in "
            "eradicating this recurrent cycle of euphoria and revulsion. For central bankers, corporate treasurers, and individual investors alike, "
            "surviving the turbulent tides of financial history demands deep institutional humility, rigorous counter-cyclical discipline, and the "
            "unflinching courage to remember that market reality always, inevitably, reasserts its gravity."
        ),
        "content_tr": (
            "Sonuç olarak, spekülatif varlık çılgınlıklarının mekaniği, finansal balonların kapitalizmin tuhaf sapmaları değil, esnek parasal kredi "
            "altında faaliyet gösteren insan psikolojisinin doğal ve içsel bir sonucu olduğunu göstermektedir. Hollanda lale soğanlarından ve Güney "
            "Denizi Şirketinden 1929 Wall Street çöküşüne, 2000 Dot-Com çılgınlığına ve modern spekülatif kripto para furyalarına kadar yörünge değişmez "
            "kalır: yer değiştirme, kredi genişlemesi, coşkulu anlatı bulaşması, Ponzi finansmanı, kaçınılmaz Minsky Anı ve felaket düzeyinde likidite "
            "tasfiyesi. Ne gelişmiş hesaplamalı finansal modelleme ne de sofistike algoritmik türevler, bu tekrarlayan öfori ve nefret döngüsünü ortadan "
            "kaldırmayı başaramamıştır. Merkez bankacıları, kurumsal hazineciler ve bireysel yatırımcılar için finans tarihinin çalkantılı gelgitlerinde "
            "hayatta kalmak; derin bir kurumsal tevazu, titiz bir döngü karşıtı disiplin ve piyasa gerçekliğinin her zaman, kaçınılmaz olarak kendi "
            "yerçekimini yeniden dayatacağını hatırlama yönündeki sarsılmaz cesareti gerektirir."
        )
    },

    "reading.c2.architectural-modularity-and-technical-debt": {
        "title": "Thermodynamic Mastery: Building Software to Outlive Decades",
        "content_en": (
            "In the final architectural synthesis, conquering software entropy and mastering technical debt is the ultimate measure of elite technology "
            "craftsmanship. Software codebases do not deteriorate because silicon chips wear out; they degrade because human organizational complexity, "
            "deadline shortcuts, and leaky modular abstractions relentlessly corrode structural coherence over time. By faithfully institutionalizing David "
            "Parnas's information hiding doctrines, enforcing high cohesion and loose coupling through Hexagonal domain purity, automating architectural "
            "fitness functions within CI/CD pipelines, and dedicating deliberate sprint capacity to continuous refactoring, engineering organizations "
            "defy Lehman's Law of Increasing Entropy. Pristine architectural modularity transforms enterprise software from a depreciating, fragile liability "
            "into a vibrant, antifragile asset capable of compounding commercial value across generations of technological innovation."
        ),
        "content_tr": (
            "Nihai mimari sentezde, yazılım entropisini fethetmek ve teknik borçta ustalaşmak, seçkin teknoloji işçiliğinin nihai ölçüsüdür. Yazılım "
            "kod tabanları silikon çipler yıprandığı için bozulmaz; insani örgütsel karmaşıklık, teslim tarihi kestirmeleri ve sızıntılı modüler soyutlamalar "
            "zamanla yapısal tutarlılığı amansızca aşındırdığı için bozulurlar. David Parnas'ın bilgi gizleme doktrinlerini sadakatle kurumsallaştırarak, "
            "Altıgen alan saflığı yoluyla yüksek bağıntı ve gevşek bağımlılığı uygulayarak, CI/CD boru hatlarında mimari uygunluk fonksiyonlarını otomatikleştirerek "
            "ve bilinçli sprint kapasitesini sürekli yeniden yapılandırmaya adayarak mühendislik organizasyonları; Lehman'ın Artan Entropi Yasası'na meydan "
            "okurlar. Bozulmamış mimari modülerlik, kurumsal yazılımı değer kaybeden kırılgan bir yükümlülükten, nesiller boyu süren teknolojik yenilikler "
            "boyunca ticari değer üretebilen canlı, anti-kırılgan bir varlığa dönüştürür."
        )
    },

    "reading.c2.algorithmic-governance-and-ethics": {
        "title": "Preserving the Soul of Justice in the Automated Era",
        "content_en": (
            "To conclude, the jurisprudence of algorithmic governance constitutes the defining constitutional challenge of twenty-first-century democracy. "
            "As sovereign discretion, judicial adjudication, and economic opportunity are increasingly mediated by high-dimensional deep neural networks, "
            "societies must reject the seductive technocratic myth that statistical prediction is identical to substantive justice. Algorithms optimize for "
            "historical pattern replication; justice requires moral imagination, human empathy, and the contestable articulation of reasons. By establishing "
            "the non-negotiable human Right to Explanation, advancing the frontiers of mechanistic interpretability, auditing training corpora for structural "
            "disparities, and strictly prohibiting algorithmic autonomy in life-and-death legal decisions, democratic civilizations safeguard the immutable "
            "primacy of human dignity over automated mathematical hegemony."
        ),
        "content_tr": (
            "Sonuç olarak, algoritmik yönetişim içtihadı yirmi birinci yüzyıl demokrasisinin belirleyici anayasal meydan okumasını oluşturmaktadır. Egemen "
            "takdir yetkisi, adli yargılama ve ekonomik fırsat giderek yüksek boyutlu derin sinir ağları tarafından yönlendirildikçe toplumlar; istatistiksel "
            "tahminin maddi adaletle özdeş olduğu yönündeki baştan çıkarıcı teknokratik miti reddetmelidir. Algoritmalar tarihsel örüntü replikasyonu "
            "için optimize edilir; adalet ise ahlaki hayal gücü, insani empati ve gerekçelerin itiraz edilebilir şekilde ifade edilmesini talep eder. "
            "Pazarlık konusu edilemez insani Açıklama Hakkı'nı tesis ederek, mekanistik yorumlanabilirliğin sınırlarını ilerleterek, eğitim külliyatlarını "
            "yapısal eşitsizlikler açısından denetleyerek ve ölüm kalım hukuki kararlarında algoritmik özerkliği kesinlikle yasaklayarak demokratik "
            "medeniyetler; insan onurunun otomatikleştirilmiş matematiksel hegemonya üzerindeki değişmez önceliğini güvence altına alırlar."
        )
    }
}

def run():
    reading_dir = project_root / "content" / "reading"
    yaml_files = sorted(reading_dir.rglob("*.yaml"))

    total_articles = 0
    gt_1000 = 0
    results = []

    for ypath in yaml_files:
        if "batches" in ypath.parts or "samples" in ypath.parts:
            continue

        with open(ypath, "r", encoding="utf-8") as f:
            articles = yaml.safe_load(f)

        if not isinstance(articles, list):
            continue

        modified = False
        for a in articles:
            a_id = a.get("id", "")
            total_articles += 1

            if a_id in EXTRA_PARAS:
                # Add as the final paragraph
                paras = a.get("paragraphs", [])
                extra = EXTRA_PARAS[a_id]
                extra_dict = {
                    "paragraph_index": len(paras) + 1,
                    "title": extra["title"],
                    "content_en": extra["content_en"],
                    "content_tr": extra["content_tr"]
                }
                paras.append(extra_dict)
                a["paragraphs"] = paras
                modified = True

            actual_words = sum(len(p.get("content_en", "").split()) for p in a.get("paragraphs", []))
            a["word_count"] = actual_words
            a["estimated_reading_minutes"] = max(1, int(round(actual_words / 180)))
            modified = True

            if actual_words > 1000:
                gt_1000 += 1
            results.append((a_id, a.get("cefr_level"), actual_words))

        if modified:
            with open(ypath, "w", encoding="utf-8") as f:
                yaml.dump(articles, f, allow_unicode=True, sort_keys=False, width=120)
            print(f"[SAVED] {ypath.name}")

    print("\n==================================================")
    print("Reading Hardening Verification")
    print("==================================================")
    print(f"Total Reading Articles: {total_articles}")
    print(f"Articles genuinely > 1,000 words: {gt_1000}")
    print(f"Average article word count: {sum(w for _, _, w in results) / len(results):.1f} words")
    print("\nArticles > 1,000 words list:")
    for a_id, cefr, words in sorted(results, key=lambda x: -x[2]):
        if words > 1000:
            print(f"  - {a_id} ({cefr}): {words} words")

if __name__ == "__main__":
    run()
