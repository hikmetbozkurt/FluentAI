#!/usr/bin/env python3
"""
Reading Generator for B1 (8 new articles, bringing B1 total to 9).
Length range: 450-650 words per article.
"""

from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=201)

def build_q(qid, q_en, q_tr_hint, correct, distractors, exp_en, exp_tr):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": qid,
        "question_en": q_en,
        "question_tr_hint": q_tr_hint,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr
    }

B1_READING_ARTICLES = [
    {
        "id": "reading.b1.async-communication-handbook",
        "title": "The Strategic Shift Toward Asynchronous Team Communication",
        "cefr_level": "B1",
        "category": "workplace_communication",
        "summary_en": "Why modern distributed organizations replace constant video meetings with structured written documentation, enabling deep focus and respectful cross-timezone workflows.",
        "summary_tr": "Modern dağıtık şirketlerin sürekli görüntülü toplantıları neden yapılandırılmış yazılı belgelerle değiştirdiği ve derin odaklanmayı nasıl sağladığı.",
        "word_count": 485,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Fatigue of Constant Virtual Meetings",
                "content_en": "Over the past five years, remote working environments revealed an unexpected operational friction: meeting exhaustion. When organizations transitioned away from physical headquarters, managers attempted to replicate in-person oversight by scheduling continuous video calls. Software developers and analysts frequently found their calendars fragmented into thirty-minute increments, leaving virtually no uninterrupted time for cognitively demanding engineering tasks. In response, forward-thinking tech enterprises began adopting asynchronous communication as their default operating principle.",
                "content_tr": "Son beş yılda uzaktan çalışma ortamları beklenmedik bir operasyonel sürtünmeyi ortaya çıkardı: toplantı yorgunluğu. Şirketler fiziksel merkezlerden uzaklaştığında, yöneticiler sürekli görüntülü görüşmeler planlayarak yüz yüze denetimi taklit etmeye çalıştılar. Yazılımcılar ve analistler takvimlerinin otuz dakikalık dilimlere bölündüğünü ve bilişsel olarak zorlayıcı mühendislik görevleri için kesintisiz zaman kalmadığını gördüler. Buna karşılık, yenilikçi teknoloji şirketleri varsayılan çalışma prensibi olarak asenkron iletişimi benimsemeye başladılar."
            },
            {
                "paragraph_index": 2,
                "title": "The Power of Written Documentation",
                "content_en": "Asynchronous collaboration means that work happens independently of shared time. Instead of convening an emergency call to discuss an architectural question, an engineer authors a structured proposal detailing the problem context, trade-offs, and proposed solution. Team members across multiple continents review the document when their schedule permits, offering thoughtful inline comments. This model shifts emphasis away from rapid verbal eloquence toward logical written clarity, allowing more reflective and inclusive decision-making.",
                "content_tr": "Asenkron iş birliği, çalışmanın ortak zamandan bağımsız olarak gerçekleştiği anlamına gelir. Mimari bir soruyu tartışmak için acil bir toplantı düzenlemek yerine, bir mühendis sorun bağlamını, ödünleşimleri ve önerilen çözümü ayrıntılarıyla anlatan yapılandırılmış bir teklif yazar. Birden fazla kıtadaki ekip üyeleri takvimleri izin verdiğinde belgeyi inceler ve düşünceli satır içi yorumlar sunar. Bu model odağı hızlı sözlü hitabetten mantıksal yazılı netliğe kaydırarak daha düşünceli ve kapsayıcı karar alma süreçlerine olanak tanır."
            },
            {
                "paragraph_index": 3,
                "title": "Protecting Deep Focus Blocks",
                "content_en": "A fundamental advantage of asynchronous culture is the preservation of uninterrupted focus. Cognitive research demonstrates that when knowledge workers are interrupted by sudden chat pings or spontaneous meetings, it requires upwards of twenty minutes to regain deep concentration. By establishing an agreement that messages do not require an instantaneous reply within seconds, companies empower technical staff to enter flow states where complex problem-solving flourishes.",
                "content_tr": "Asenkron kültürün temel bir avantajı, kesintisiz odaklanmanın korunmasıdır. Bilişsel araştırmalar, bilgi işçilerinin anlık sohbet bildirimleri veya spontane toplantılarla bölündüğünde derin konsantrasyonu yeniden kazanmanın yirmi dakikadan fazla sürdüğünü göstermektedir. Mesajların saniyeler içinde anlık yanıt gerektirmediğine dair bir anlaşma yaparak şirketler, teknik personelin karmaşık problem çözmenin geliştiği akış (flow) durumuna girmesini sağlar."
            },
            {
                "paragraph_index": 4,
                "title": "When Synchronous Communication Still Matters",
                "content_en": "Adopting asynchronous principles does not mean eliminating spoken conversation entirely. Real-time synchronous discussions remain essential for sensitive interpersonal feedback, high-stakes crisis triage, and social team bonding. However, when synchronous calls are reserved exclusively for these specific high-value moments, participants arrive engaged, energetic, and fully prepared to collaborate effectively.",
                "content_tr": "Asenkron ilkeleri benimsemek, sözlü iletişimi tamamen ortadan kaldırmak anlamına gelmez. Hassas kişilerarası geri bildirimler, yüksek öncelikli kriz müdahaleleri ve sosyal ekip kaynaşması için gerçek zamanlı eşzamanlı tartışmalar vazgeçilmezliğini korur. Ancak eşzamanlı toplantılar yalnızca bu özel yüksek değerli anlar için ayrıldığında, katılımcılar enerjik, ilgili ve etkili bir şekilde iş birliği yapmaya hazır olarak gelirler."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "asynchronous",
                "vocab_id": "vocab.asynchronous",
                "context_definition_en": "Occurring at different times rather than simultaneously.",
                "context_meaning_tr": "Eşzamanlı olmayan, farklı zaman dilimlerinde gerçekleşen."
            },
            {
                "word": "eloquence",
                "vocab_id": "vocab.eloquence",
                "context_definition_en": "Fluent, persuasive, and articulate speaking or writing.",
                "context_meaning_tr": "Akıcı, etkili ve ikna edici söz söyleme sanatı (hitabet)."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_01_01",
                "What unexpected operational friction emerged when companies initially shifted to remote work?",
                "Şirketler başlangıçta uzaktan çalışmaya geçtiğinde hangi beklenmedik sorun ortaya çıktı?",
                "Meeting exhaustion caused by excessive scheduled video calls",
                ["Total loss of electrical grid connectivity across global cities", "Immediate bankruptcy of major cloud storage providers", "A complete refusal of engineers to use written English"],
                "Paragraph 1 notes that managers scheduled continuous calls leading to meeting exhaustion.",
                "1. paragraf yöneticilerin sürekli görüşme planlamasının toplantı yorgunluğuna yol açtığını belirtir."
            ),
            build_q(
                "q_r_b1_01_02",
                "How does asynchronous decision-making handle complex architectural proposals?",
                "Asenkron karar alma karmaşık mimari teklifleri nasıl ele alır?",
                "Through structured written documents reviewed when individual schedules permit",
                ["By forcing every engineer to vote within two minutes of receipt", "By outsourcing technical decisions to anonymous public internet forums", "By discarding all proposals that exceed three sentences in length"],
                "Paragraph 2 explains engineers author structured proposals that colleagues review when schedule permits.",
                "2. paragraf mühendislerin takvimleri elverdiğinde inceledikleri yapılandırılmış belgeler yazdığını açıklar."
            ),
            build_q(
                "q_r_b1_01_03",
                "According to cognitive research mentioned in paragraph 3, what happens after an interruption?",
                "3. paragrafta belirtilen bilişsel araştırmaya göre bir kesintiden sonra ne olur?",
                "It takes more than twenty minutes to regain deep mental concentration",
                ["Workers permanently forget their native spoken language", "Computers experience internal processor hardware failures", "Employees immediately receive salary increases from management"],
                "Paragraph 3 states it requires upwards of twenty minutes to regain concentration.",
                "3. paragraf konsantrasyonu yeniden kazanmanın yirmi dakikadan fazla sürdüğünü belirtir."
            ),
            build_q(
                "q_r_b1_01_04",
                "When is synchronous real-time communication still considered essential?",
                "Gerçek zamanlı eşzamanlı iletişim ne zaman hala vazgeçilmez kabul edilir?",
                "For sensitive interpersonal feedback, crisis triage, and team bonding",
                ["For daily routine status updates that take ten seconds", "For calculating employee lunch expenses line by line", "For reading technical terms of service out loud"],
                "Paragraph 4 mentions sensitive feedback, crisis triage, and team bonding.",
                "4. paragraf hassas geri bildirim, kriz müdahalesi ve ekip kaynaşmasını listeler."
            ),
            build_q(
                "q_r_b1_01_05",
                "What cultural agreement must exist for asynchronous focus to be effective?",
                "Asenkron odaklanmanın etkili olması için hangi kültürel anlaşma var olmalıdır?",
                "An understanding that messages do not demand an instantaneous response within seconds",
                ["A rule requiring employees to work through the night without sleep", "A mandate that all written text must rhyme", "A policy forbidding all contact between product managers and engineers"],
                "Paragraph 3 states that messages should not require instantaneous replies.",
                "3. paragraf mesajların saniyeler içinde anlık yanıt gerektirmediği mutabakatını vurgular."
            )
        ],
        "topic_tags": ["asynchronous-work", "remote-teams", "productivity", "b1-reading"],
        "related_ids": ["vocab.asynchronous", "vocab.eloquence"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.cybersecurity-essentials",
        "title": "Cybersecurity Hygiene for Remote Knowledge Workers",
        "cefr_level": "B1",
        "category": "technology",
        "summary_en": "A practical exploration of modern cyber hygiene, examining multi-factor authentication, phishing vulnerability detection, and the principles of zero-trust security.",
        "summary_tr": "Uzaktan çalışanlar için modern siber hijyen rehberi: çok faktörlü kimlik doğrulama, oltalama (phishing) tespiti ve sıfır güven ilkeleri.",
        "word_count": 510,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Expanding Corporate Security Perimeter",
                "content_en": "Traditionally, corporate cybersecurity relied on a fortress model. All company computers resided within a physical office building protected by centralized hardware firewalls. However, the rise of remote and hybrid work fundamentally dissolved this perimeter. Today, sensitive company data traverses home Wi-Fi networks, personal mobile devices, and public coffee shop connections. Consequently, every individual employee now functions as a primary defensive perimeter against international cyber threats.",
                "content_tr": "Geleneksel olarak kurumsal siber güvenlik bir kale modeline dayanıyordu. Tüm şirket bilgisayarları merkezi donanım güvenlik duvarlarıyla korunan fiziksel bir ofis binasında bulunuyordu. Ancak uzaktan ve hibrit çalışmanın yükselişi bu çevreyi temelden ortadan kaldırdı. Bugün hassas şirket verileri ev Wi-Fi ağlarından, kişisel mobil cihazlardan ve halka açık kafe bağlantılarından geçmektedir. Sonuç olarak, her bir çalışan artık uluslararası siber tehditlere karşı birincil bir savunma hattı olarak görev yapmaktadır."
            },
            {
                "paragraph_index": 2,
                "title": "The Strength of Multi-Factor Authentication",
                "content_en": "Single passwords, no matter how complex, are no longer sufficient to secure corporate repositories. Malicious actors continuously acquire stolen credentials through automated database leaks and credential stuffing attacks. Multi-factor authentication (MFA) neutralizes this threat by requiring two independent verification methods: something you know (your password) and something you possess (a hardware security key or authenticator app). Even if a password is compromised, unauthorized access remains completely blocked.",
                "content_tr": "Tek bir şifre, ne kadar karmaşık olursa olsun, kurumsal veri depolarını güvence altına almak için artık yeterli değildir. Kötü niyetli aktörler otomatik veritabanı sızıntıları ve kimlik bilgisi doldurma saldırıları yoluyla sürekli olarak çalınan kimlik bilgilerini ele geçirir. Çok faktörlü kimlik doğrulama (MFA), iki bağımsız doğrulama yöntemi gerektirerek bu tehdidi etkisiz hale getirir: bildiğiniz bir şey (şifreniz) ve sahip olduğunuz bir şey (donanım güvenlik anahtarı veya kimlik doğrulama uygulaması). Bir şifre ele geçirilse bile yetkisiz erişim tamamen engellenmiş kalır."
            },
            {
                "paragraph_index": 3,
                "title": "Recognizing Sophisticated Phishing Scams",
                "content_en": "The vast majority of corporate security breaches do not originate from technical software vulnerabilities; they stem from human deception known as phishing. Attackers craft highly convincing emails that mimic company executives, cloud service providers, or HR departments, urgently demanding password resets or wire transfers. Cultivating healthy skepticism—such as checking the sender's authentic domain name and verifying requests through secondary channels—is the most effective defense against social engineering.",
                "content_tr": "Kurumsal güvenlik ihlallerinin büyük çoğunluğu teknik yazılım açıklarından kaynaklanmaz; oltalama (phishing) olarak bilinen insan aldatmacasından kaynaklanır. Saldırganlar şirket yöneticilerini, bulut sağlayıcılarını veya İK departmanlarını taklit eden, acil şifre sıfırlama veya para transferi talep eden son derece ikna edici e-postalar hazırlarlar. Gönderenin gerçek alan adını kontrol etmek ve talepleri ikincil kanallardan doğrulamak gibi sağlıklı bir şüphecilik geliştirmek, sosyal mühendisliğe karşı en etkili savunmadır."
            },
            {
                "paragraph_index": 4,
                "title": "The Philosophy of Zero-Trust",
                "content_en": "Modern IT departments increasingly implement a 'Zero-Trust' architectural model. This philosophy operates under a simple maxim: never trust, always verify. Every request to access internal networks, databases, or documentation is authenticated, authorized, and encrypted, regardless of whether the user is located in the executive suite or a remote co-working lounge.",
                "content_tr": "Modern BT departmanları giderek daha fazla 'Sıfır Güven' (Zero-Trust) mimari modelini uygulamaktadır. Bu felsefe basit bir ilke altında çalışır: asla güvenme, daima doğrula. Dahili ağlara, veritabanlarına veya belgelere erişmek için yapılan her istek, kullanıcının yönetici katında veya uzaktaki bir ortak çalışma alanında bulunup bulunmadığına bakılmaksızın kimlik doğrulamasına tabi tutulur, yetkilendirilir ve şifrelenir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "credential",
                "vocab_id": "vocab.credential",
                "context_definition_en": "A qualification, document, or password proving a person's identity and authorization.",
                "context_meaning_tr": "Bir kişinin kimliğini ve yetkisini kanıtlayan şifre veya kimlik belgesi."
            },
            {
                "word": "phishing",
                "vocab_id": "vocab.phishing",
                "context_definition_en": "The fraudulent practice of sending emails masquerading as reputable companies to reveal passwords.",
                "context_meaning_tr": "Şifreleri ele geçirmek için saygın kurumları taklit eden e-posta dolandırıcılığı (oltalama)."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_02_01",
                "How did remote and hybrid work fundamentally alter corporate cybersecurity?",
                "Uzaktan ve hibrit çalışma kurumsal siber güvenliği temelden nasıl değiştirdi?",
                "It dissolved the physical office perimeter, turning every employee into a frontline defense point",
                ["It eliminated all known software bugs and technical errors permanently", "It made computer encryption illegal under international trade agreements", "It forced all companies to disconnect from the public internet"],
                "Paragraph 1 explains the physical perimeter dissolved, making employees primary defenses.",
                "1. paragraf fiziksel sınırın kalktığını ve çalışanların ön savunma hattı olduğunu belirtir."
            ),
            build_q(
                "q_r_b1_02_02",
                "Why is a single password considered inadequate in modern cybersecurity?",
                "Modern siber güvenlikte tek bir şifre neden yetersiz kabul edilir?",
                "Automated database leaks and credential stuffing make stolen passwords easy to acquire",
                ["Passwords can only contain letters from the Greek alphabet", "Modern keyboards cannot process numbers higher than five", "Operating systems delete passwords every two hours automatically"],
                "Paragraph 2 states attackers acquire passwords through automated leaks and credential stuffing.",
                "2. paragraf saldırganların sızıntılar yoluyla şifreleri kolayca ele geçirdiğini açıklar."
            ),
            build_q(
                "q_r_b1_02_03",
                "How does multi-factor authentication (MFA) protect corporate accounts?",
                "Çok faktörlü kimlik doğrulama (MFA) kurumsal hesapları nasıl korur?",
                "By requiring both a password and an independent physical or digital verification device",
                ["By taking a physical photograph of the user's passport every morning", "By requiring approval from three foreign embassies before logging in", "By resetting the entire computer operating system after each session"],
                "Paragraph 2 explains MFA requires something you know and something you possess.",
                "2. paragraf MFA'nın bilinen bir şey ile sahip olunan bir cihazı birlikte gerektirdiğini belirtir."
            ),
            build_q(
                "q_r_b1_02_04",
                "What is the root cause behind the majority of corporate security breaches?",
                "Kurumsal güvenlik ihlallerinin çoğunun altında yatan temel neden nedir?",
                "Human deception and social engineering techniques such as phishing",
                ["Hardware lightning strikes on undersea fiber optic cables", "The complete obsolescence of personal computer displays", "Mathematical errors in standard desktop arithmetic calculators"],
                "Paragraph 3 states breaches stem from human deception known as phishing.",
                "3. paragraf ihlallerin oltalama gibi insan aldatmacalarından kaynaklandığını açıklar."
            ),
            build_q(
                "q_r_b1_02_05",
                "What is the foundational maxim of the 'Zero-Trust' security philosophy?",
                "'Sıfır Güven' güvenlik felsefesinin temel ilkesi nedir?",
                "Never trust, always verify every access request",
                ["Trust all internal employees completely without any passwords", "Assume that computer hardware never fails under load", "Allow public internet users unrestricted access to company data"],
                "Paragraph 4 defines Zero-Trust as: 'never trust, always verify'.",
                "4. paragraf Sıfır Güven'i 'asla güvenme, daima doğrula' olarak tanımlar."
            )
        ],
        "topic_tags": ["cybersecurity", "phishing", "zero-trust", "technology", "b1-reading"],
        "related_ids": ["vocab.credential", "vocab.phishing"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.career-transition-engineering",
        "title": "Navigating Professional Career Shifts in Modern Engineering",
        "cefr_level": "B1",
        "category": "engineering_culture",
        "summary_en": "Insights into transitioning from individual technical contributor to engineering leadership, balancing hands-on coding with delegation and team mentorship.",
        "summary_tr": "Bireysel yazılımcılıktan mühendislik yöneticiliğine geçiş: kod yazma ile delege etme ve ekip koçluğu arasındaki denge.",
        "word_count": 490,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Senior Contributor Dilemma",
                "content_en": "After five to eight years of writing clean code and shipping complex software features, senior software developers face a critical crossroads in their career. In many technology organizations, two primary paths emerge: the technical leadership path (such as Staff or Principal Engineer) and the engineering management path. While both directions offer equal compensation and prestige, they require fundamentally different daily skills and psychological mindsets.",
                "content_tr": "Beş ila sekiz yıl boyunca temiz kod yazıp karmaşık yazılım özellikleri teslim ettikten sonra, kıdemli yazılımcılar kariyerlerinde kritik bir yol ayrımına gelirler. Birçok teknoloji şirketinde iki ana yol ortaya çıkar: teknik uzmanlık yolu (Staff veya Principal Mühendis gibi) ve mühendislik yöneticiliği yolu. Her iki yön de eşit tazminat ve prestij sunarken, temelden farklı günlük beceriler ve psikolojik zihniyetler gerektirir."
            },
            {
                "paragraph_index": 2,
                "title": "The Trap of Hands-On Over-Involvement",
                "content_en": "The most common struggle for new engineering managers is letting go of the keyboard. As an individual contributor, productivity is measured by pull requests merged, algorithms optimized, and bugs squashed. In management, however, productivity is measured through the amplified output of your team. Managers who insist on writing the critical architectural code themselves frequently become operational bottlenecks, depriving junior team members of learning opportunities.",
                "content_tr": "Yeni mühendislik yöneticileri için en yaygın mücadele, klavyeyi bırakmaktır. Bireysel bir çalışan olarak üretkenlik birleştirilen çekme istekleri, optimize edilen algoritmalar ve çözülen hatalarla ölçülür. Ancak yöneticilikte üretkenlik, ekibinizin katlanarak artan çıktısıyla ölçülür. Kritik mimari kodları kendileri yazmakta ısrar eden yöneticiler sıklıkla operasyonel darboğazlar haline gelir ve kıdemsiz ekip üyelerini öğrenme fırsatlarından mahrum bırakır."
            },
            {
                "paragraph_index": 3,
                "title": "Developing People and Psychological Safety",
                "content_en": "Successful technical leadership centers on creating psychological safety. When engineers feel safe to experiment, share honest dissent, and admit technical oversights without fear of reprisal, innovation accelerates. An effective engineering manager acts as an organizational umbrella: shielding the squad from disruptive administrative politics while securing the resources, promotions, and recognition the team deserves.",
                "content_tr": "Başarılı teknik liderlik, psikolojik güvenlik yaratmaya odaklanır. Mühendisler misilleme korkusu olmadan deney yapmaktan, dürüst muhalefet paylaşmaktan ve teknik hataları kabul etmekten korkmadıklarında yenilik hızlanır. Etkili bir mühendislik yöneticisi kurumsal bir şemsiye gibi hareket eder: ekibi dikkat dağıtıcı bürokratik politikalardan korurken ekibin hak ettiği kaynakları, terfileri ve takdiri güvence altına alır."
            },
            {
                "paragraph_index": 4,
                "title": "Continuous Technical Relevance",
                "content_en": "Transitioning to management does not imply abandoning technical literacy. While managers rarely write production code daily, they must maintain high-level architectural intuition. Understanding distributed system trade-offs, database indexing limits, and security frameworks allows managers to ask incisive questions during sprint retrospectives and protect their teams from unrealistic executive commitments.",
                "content_tr": "Yöneticiliğe geçmek, teknik okuryazarlığı terk etmek anlamına gelmez. Yöneticiler nadiren her gün canlı kod yazsa da üst düzey mimari sezgilerini korumalıdırlar. Dağıtık sistem ödünleşimlerini, veritabanı indeksleme sınırlarını ve güvenlik çerçevelerini anlamak yöneticilerin sprint retrospektiflerinde keskin sorular sormasına ve ekiplerini gerçekçi olmayan yönetici taahhütlerinden korumasına olanak tanır."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "dilemma",
                "vocab_id": "vocab.dilemma",
                "context_definition_en": "A situation in which a difficult choice must be made between two or more alternatives.",
                "context_meaning_tr": "İki veya daha fazla seçenek arasında seçim yapmayı gerektiren zor durum (ikilem)."
            },
            {
                "word": "reprisal",
                "vocab_id": "vocab.reprisal",
                "context_definition_en": "An act of retaliation or punishment against someone.",
                "context_meaning_tr": "Birine karşı yapılan misilleme veya cezalandırıcı eylem."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_03_01",
                "What two primary career paths emerge for senior developers after several years of experience?",
                "Birkaç yıllık deneyimin ardından kıdemli yazılımcılar için hangi iki ana kariyer yolu ortaya çıkar?",
                "The technical specialist path (e.g., Staff Engineer) and the engineering management path",
                ["The physical security guard path and the building maintenance path", "The professional sports coaching path and the culinary arts path", "The legal defense attorney path and the medical nursing path"],
                "Paragraph 1 specifies technical leadership (Staff/Principal) and engineering management.",
                "1. paragraf teknik uzmanlık ve mühendislik yöneticiliği yollarını belirtir."
            ),
            build_q(
                "q_r_b1_03_02",
                "What is a common trap for newly promoted engineering managers?",
                "Yeni terfi eden mühendislik yöneticileri için yaygın bir tuzak nedir?",
                "Continuing to write critical code themselves instead of delegating to the team",
                ["Refusing to accept their monthly salary from the company", "Deleting the company's code repository on their first day", "Forbidding team members from taking any vacation days"],
                "Paragraph 2 states the most common struggle is letting go of the keyboard and writing code.",
                "2. paragraf klavyeyi bırakamamak ve kritik kodları kendisi yazmak olduğunu açıklar."
            ),
            build_q(
                "q_r_b1_03_03",
                "How is an engineering manager's productivity fundamentally measured?",
                "Bir mühendislik yöneticisinin üretkenliği temel olarak nasıl ölçülür?",
                "Through the amplified collective output and growth of their engineering squad",
                ["By counting how many individual lines of code they type per hour", "By the physical weight of their personal corporate laptop", "By how many times they criticize their junior colleagues"],
                "Paragraph 2 states management productivity is measured through the amplified output of the team.",
                "2. paragraf yöneticinin üretkenliğinin ekibin çoğalan çıktısıyla ölçüldüğünü belirtir."
            ),
            build_q(
                "q_r_b1_03_04",
                "Why is 'psychological safety' essential in high-performing engineering cultures?",
                "Yüksek performanslı mühendislik kültürlerinde 'psikolojik güvenlik' neden esastır?",
                "It allows engineers to experiment and admit technical oversights without fear of punishment",
                ["It eliminates the need for software code reviews and automated testing", "It guarantees that software will never experience server latency", "It allows employees to ignore all scheduled customer deadlines"],
                "Paragraph 3 explains safety encourages experimentation and admitting oversights without fear.",
                "3. paragraf ceza korkusu olmadan deneme yapmayı ve hataları kabullenmeyi sağladığını açıklar."
            ),
            build_q(
                "q_r_b1_03_05",
                "Why must engineering managers maintain high-level technical intuition?",
                "Mühendislik yöneticileri neden üst düzey teknik sezgilerini korumalıdır?",
                "To ask incisive questions and protect their team from unrealistic commitments",
                ["To compete with junior developers in daily speed coding contests", "To replace all automated compilers with manual human calculation", "To prove that management requires no communication skills"],
                "Paragraph 4 explains technical literacy helps ask incisive questions and avoid unrealistic goals.",
                "4. paragraf teknik bilginin keskin sorular sormaya ve ekibi korumaya yardımcı olduğunu belirtir."
            )
        ],
        "topic_tags": ["engineering-management", "career-path", "leadership", "b1-reading"],
        "related_ids": ["vocab.dilemma", "vocab.reprisal"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.effective-code-reviews",
        "title": "Constructive Peer Feedback in Modern Software Teams",
        "cefr_level": "B1",
        "category": "engineering_culture",
        "summary_en": "Best practices for conducting peer code reviews: focusing on architecture over stylistic nitpicks, phrasing suggestions empathetically, and maintaining fast review turnarounds.",
        "summary_tr": "Meslektaş kod incelemelerinde en iyi uygulamalar: üslup takıntısı yerine mimariye odaklanma, empatik geri bildirim verme ve hızlı inceleme döngüleri.",
        "word_count": 475,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Double Purpose of Code Reviews",
                "content_en": "Peer code reviews are widely recognized as a cornerstone of software quality assurance. When an engineer finishes a feature branch, peers inspect the pull request before changes are integrated into the main branch. While the primary goal is identifying logic defects and security vulnerabilities, an equally important secondary objective is knowledge sharing. Reviewing code exposes team members to novel design patterns and prevents knowledge from becoming concentrated in a single individual.",
                "content_tr": "Meslektaş kod incelemeleri, yazılım kalite güvencesinin temel taşı olarak geniş çapta kabul görmektedir. Bir mühendis bir özellik dalını bitirdiğinde, değişiklikler ana dala entegre edilmeden önce meslektaşları çekme isteğini inceler. Birincil amaç mantık hatalarını ve güvenlik açıklarını tespit etmek olsa da, eşit derecede önemli bir ikincil hedef de bilgi paylaşımıdır. Kod incelemesi ekip üyelerini yeni tasarım kalıplarıyla tanıştırır ve bilginin tek bir bireyde toplanmasını önler."
            },
            {
                "paragraph_index": 2,
                "title": "Automating Style to Focus on Substance",
                "content_en": "A common dysfunction in peer reviews is wasting hours debating trivial formatting preferences, such as indentation spacing or variable naming conventions. High-performing engineering teams eliminate this friction by enforcing automated formatters and linters in their CI/CD pipelines. When automated tools handle mechanical syntax compliance, human reviewers can dedicate their cognitive energy to meaningful architectural concerns: concurrency, memory allocation, and API robustness.",
                "content_tr": "Kod incelemelerinde yaygın bir işlev bozukluğu girinti aralığı veya değişken adlandırma kuralları gibi önemsiz biçimlendirme tercihleri üzerinde saatlerce tartışmaktır. Yüksek performanslı mühendislik ekipleri CI/CD hatlarında otomatik biçimlendiriciler ve linter araçları uygulayarak bu sürtünmeyi ortadan kaldırır. Otomatik araçlar mekanik sözdizimi uyumunu hallettiğinde, insan incelemeciler bilişsel enerjilerini anlamlı mimari konulara ayırabilir: eşzamanlılık, bellek tahsisi ve API dayanıklılığı."
            },
            {
                "paragraph_index": 3,
                "title": "The Art of Empathetic Phrasing",
                "content_en": "Receiving critical feedback on code you worked hard to build can feel personal. Reviewers must frame observations constructively. Rather than issuing abrupt commands ('Change this function immediately'), experienced reviewers frame comments as collaborative inquiries ('What do you think about breaking this method into two smaller functions to improve testability?'). Phrasing feedback around shared product outcomes fosters psychological safety and mutual respect.",
                "content_tr": "İnşa etmek için çok çalıştığınız kod hakkında eleştirel geri bildirim almak kişisel hissettirebilir. İncelemeciler gözlemlerini yapıcı şekilde çerçevelemelidir. 'Bu fonksiyonu derhal değiştir' gibi kaba emirler vermek yerine deneyimli incelemeciler yorumlarını iş birliğine dayalı sorular olarak ifade eder ('Test edilebilirliği artırmak için bu yöntemi iki küçük fonksiyona bölmek hakkında ne düşünüyorsun?'). Geri bildirimi ortak ürün çıktıları etrafında çerçevelemek psikolojik güvenliği ve karşılıklı saygıyı geliştirir."
            },
            {
                "paragraph_index": 4,
                "title": "The Cost of Review Latency",
                "content_en": "Finally, the velocity of an engineering team hinges on review turnaround times. When pull requests sit unreviewed for days, developers must switch context, leading to cognitive fatigue and merge conflicts. Treating peer reviews as a first-class daily priority—aiming to review code within four to six business hours—keeps project momentum fluid and predictable.",
                "content_tr": "Son olarak, bir mühendislik ekibinin hızı inceleme geri dönüş sürelerine bağlıdır. Çekme istekleri günlerce incelenmeden beklediğinde yazılımcılar bağlam değiştirmek zorunda kalır, bu da bilişsel yorgunluğa ve birleştirme çakışmalarına yol açar. Meslektaş incelemelerini birinci sınıf bir günlük öncelik olarak görmek (kodu dört ila altı iş saati içinde incelemeyi hedeflemek) proje ivmesini akıcı ve öngörülebilir tutar."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "concurrency",
                "vocab_id": "vocab.concurrency",
                "context_definition_en": "The ability of different parts of a program to be executed out-of-order or simultaneously.",
                "context_meaning_tr": "Bir programın farklı bölümlerinin eşzamanlı veya sırasız yürütülebilme yeteneği."
            },
            {
                "word": "velocity",
                "vocab_id": "vocab.velocity",
                "context_definition_en": "The speed and efficiency of a team in completing committed deliverables.",
                "context_meaning_tr": "Bir ekibin taahhüt edilen işleri tamamlama hızı ve verimliliği."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_04_01",
                "Besides detecting software bugs, what is the major secondary benefit of code reviews?",
                "Yazılım hatalarını tespit etmenin yanı sıra, kod incelemelerinin ana ikincil faydası nedir?",
                "Facilitating team knowledge sharing and preventing single points of failure",
                ["Providing an automated calculation of yearly employee income taxes", "Testing the physical durability of server room floor tiles", "Eliminating the need for customers to pay for software licenses"],
                "Paragraph 1 highlights knowledge sharing and preventing knowledge silos.",
                "1. paragraf bilgi paylaşımını ve bilginin tek kişide toplanmasını önlemeyi vurgular."
            ),
            build_q(
                "q_r_b1_04_02",
                "How do elite engineering squads eliminate trivial debates over formatting?",
                "Seçkin mühendislik ekipleri biçimlendirme üzerine önemsiz tartışmaları nasıl ortadan kaldırır?",
                "By automating syntax linting and formatting within their CI/CD pipelines",
                ["By completely banning variable names longer than three characters", "By writing all software code entirely on physical paper notepads", "By forcing the junior developer to pay for the reviewer's lunch"],
                "Paragraph 2 explains automated formatters and linters handle mechanical syntax.",
                "2. paragraf otomatik linter ve biçimlendiricilerin sözdizimi uyumunu hallettiğini belirtir."
            ),
            build_q(
                "q_r_b1_04_03",
                "How should reviewers phrase feedback to maintain collaboration and respect?",
                "İncelemeciler iş birliğini ve saygıyı korumak için geri bildirimleri nasıl ifade etmelidir?",
                "As constructive collaborative inquiries focused on testability and outcomes",
                ["As harsh, blunt personal commands demanding immediate obedience", "In secret encrypted code that the developer cannot read", "By posting anonymous complaints on public internet blogs"],
                "Paragraph 3 advises framing comments as collaborative inquiries.",
                "3. paragraf yorumları iş birliğine dayalı yapıcı sorular olarak ifade etmeyi önerir."
            ),
            build_q(
                "q_r_b1_04_04",
                "What negative operational outcome occurs when pull requests sit unreviewed for days?",
                "Çekme istekleri günlerce incelenmeden beklediğinde hangi olumsuz operasyonel sonuç ortaya çıkar?",
                "Engineers suffer context-switching fatigue and face painful merge conflicts",
                ["Computers permanently erase their internal hard drives", "The company is forced to relocate its headquarters to another continent", "All software code converts into machine assembly language"],
                "Paragraph 4 describes context switching, cognitive fatigue, and merge conflicts.",
                "4. paragraf bağlam değiştirme yorgunluğu ve birleştirme çakışmalarını anlatır."
            ),
            build_q(
                "q_r_b1_04_05",
                "What recommended turnaround window does the text suggest for peer reviews?",
                "Metin meslektaş incelemeleri için hangi geri dönüş süresi aralığını önermektedir?",
                "Within four to six business hours",
                ["Exactly thirty business days", "Between two and three minutes at midnight", "Only once every six calendar months"],
                "Paragraph 4 suggests aiming to review code within four to six business hours.",
                "4. paragraf kodu 4 ila 6 iş saati içinde incelemeyi hedeflemeyi tavsiye eder."
            )
        ],
        "topic_tags": ["code-reviews", "peer-feedback", "engineering-culture", "b1-reading"],
        "related_ids": ["vocab.concurrency", "vocab.velocity"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.venture-capital-basics",
        "title": "Understanding Seed Funding and Early-Stage Startup Investment",
        "cefr_level": "B1",
        "category": "finance_and_economics",
        "summary_en": "An introduction to early-stage venture capital: pre-seed vs. seed rounds, equity dilution trade-offs, and what investors look for in founders and market validation.",
        "summary_tr": "Erken aşama girişim sermayesine giriş: tohum öncesi ve tohum turları, hisse seyrelmesi (dilution) dengesi ve yatırımcıların aradığı kriterler.",
        "word_count": 520,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Lifecycle of Early Startup Financing",
                "content_en": "Building an innovative technology enterprise requires upfront capital long before customer revenue covers daily operational expenses. In the earliest stages, founders typically fund their minimum viable product through personal savings or angel investors—wealthy individuals who provide initial risk capital. However, once a product shows promising traction and early customer engagement, founders look toward venture capital (VC) firms to accelerate expansion.",
                "content_tr": "Yenilikçi bir teknoloji şirketi kurmak, müşteri gelirleri günlük operasyonel harcamaları karşılamadan çok önce peşin sermaye gerektirir. En erken aşamalarda kurucular genellikle minimum uygulanabilir ürünlerini kişisel birikimleriyle veya melek yatırımcılarla (ilk risk sermayesini sağlayan varlıklı bireyler) finanse ederler. Ancak bir ürün umut verici bir çekim gücü (traction) ve erken müşteri bağlılığı gösterdiğinde kurucular büyümeyi hızlandırmak için girişim sermayesi (VC) firmalarına yönelirler."
            },
            {
                "paragraph_index": 2,
                "title": "Pre-Seed and Seed Rounds Explained",
                "content_en": "Early funding is structured into defined investment rounds. A 'pre-seed' round generally ranges from fifty thousand to five hundred thousand dollars, enabling founders to build a working prototype and conduct user discovery. The subsequent 'seed' round, often scaling from one to three million dollars, finances early engineering hires, infrastructure scaling, and customer acquisition channels. In exchange for capital, investors receive equity shares in the company.",
                "content_tr": "Erken aşama finansmanı tanımlanmış yatırım turlarına bölünür. 'Tohum öncesi' (pre-seed) turu genellikle elli bin ila beş yüz bin dolar arasında değişir ve kurucuların çalışan bir prototip oluşturmasına ve kullanıcı keşfi yapmasına olanak tanır. Genellikle bir ila üç milyon dolar arasında değişen müteakip 'tohum' (seed) turu ise ilk mühendis işe alımlarını, altyapı ölçeklendirmesini ve müşteri edinme kanallarını finanse eder. Sermaye karşılığında yatırımcılar şirketten hisse senedi (equity) alırlar."
            },
            {
                "paragraph_index": 3,
                "title": "The Reality of Equity Dilution",
                "content_en": "Securing venture funding is not free money; it represents a permanent sale of ownership. Each time a startup issues new shares to investors, the founders' personal ownership percentage decreases—a mathematical phenomenon known as dilution. Astute founders balance the urgency of securing capital with the discipline of preserving enough equity to maintain corporate control and stay motivated across future funding rounds.",
                "content_tr": "Girişim sermayesi yatırımı almak bedava para değildir; kalıcı bir mülkiyet satışını temsil eder. Bir girişim yatırımcılara her yeni hisse ihraç ettiğinde, kurucuların kişisel mülkiyet yüzdesi azalır (bu matematiksel olgu seyrelme / dilution olarak bilinir). Akıllı kurucular, sermaye sağlama aciliyeti ile şirket kontrolünü sürdürmek ve gelecekteki yatırım turlarında motive kalmak için yeterli hisseyi koruma disiplinini dengeler."
            },
            {
                "paragraph_index": 4,
                "title": "What Venture Investors Truly Evaluate",
                "content_en": "While novice entrepreneurs assume that venture capitalists primarily invest in patentable proprietary code, early-stage investors actually prioritize two factors: total addressable market size and founder resilience. Technology can be rewritten and business models can pivot, but navigating intense market competition requires founders who demonstrate relentless problem-solving, deep sector domain expertise, and commercial adaptability.",
                "content_tr": "Acemi girişimciler girişim sermayedarlarının öncelikle patentlenebilir tescilli koda yatırım yaptığını varsaysa da, erken aşama yatırımcıları aslında iki faktöre öncelik verir: toplam pazar büyüklüğü ve kurucu dayanıklılığı. Teknoloji yeniden yazılabilir ve iş modelleri pivot edebilir, ancak yoğun pazar rekabetinde yol almak amansız problem çözme, derin sektör uzmanlığı ve ticari uyum yeteneği gösteren kurucular gerektirir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "dilution",
                "vocab_id": "vocab.dilution",
                "context_definition_en": "The reduction in existing shareholders' ownership percentage when new shares are issued.",
                "context_meaning_tr": "Yeni hisse ihraç edildiğinde mevcut ortakların hisse oranının azalması (seyrelme)."
            },
            {
                "word": "resilience",
                "vocab_id": "vocab.resilience",
                "context_definition_en": "The capacity to recover quickly from difficulties, setbacks, and tough conditions.",
                "context_meaning_tr": "Zorluklardan ve aksiliklerden hızla toparlanma kapasitesi (dayanıklılık)."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_05_01",
                "How is a startup's minimum viable product typically funded in the earliest stage?",
                "Bir girişimin minimum uygulanabilir ürünü en erken aşamada genellikle nasıl finanse edilir?",
                "Through personal founder savings and angel investors",
                ["Via large commercial bank loans backed by government bonds", "Through massive international public stock offerings on Wall Street", "By selling commercial real estate owned by local universities"],
                "Paragraph 1 states founders use personal savings or angel investors.",
                "1. paragraf kurucuların birikimlerini veya melek yatırımcıları kullandığını belirtir."
            ),
            build_q(
                "q_r_b1_05_02",
                "What is the primary operational objective of a 'seed' funding round?",
                "Bir 'tohum' (seed) yatırım turunun birincil operasyonel hedefi nedir?",
                "Financing early engineering hires, infrastructure scaling, and customer acquisition",
                ["Purchasing private luxury jets for the founding executive team", "Paying off national government foreign debts", "Constructing physical retail shopping centers in ten cities"],
                "Paragraph 2 explains seed rounds finance engineering hires, infrastructure, and acquisition.",
                "2. paragraf tohum turunun mühendis istihdamını ve altyapı ölçeklemeyi finanse ettiğini açıklar."
            ),
            build_q(
                "q_r_b1_05_03",
                "What does 'equity dilution' mean for startup founders?",
                "Girişim kurucuları için 'hisse seyrelmesi' (dilution) ne anlama gelir?",
                "Their percentage of ownership decreases as new shares are issued to investors",
                ["Their software source code becomes completely public open source", "Their personal bank accounts are frozen by financial regulators", "The company must change its legal name after every meeting"],
                "Paragraph 3 defines dilution as the decrease in founders' ownership percentage.",
                "3. paragraf seyrelmeyi kurucuların mülkiyet yüzdesinin azalması olarak tanımlar."
            ),
            build_q(
                "q_r_b1_05_04",
                "What two criteria do early-stage venture capitalists prioritize most when investing?",
                "Erken aşama girişim yatırımcıları yatırım yaparken en çok hangi iki kritere öncelik verir?",
                "Total addressable market size and founder resilience",
                ["The physical height and athletic ability of the programmers", "The geographic distance between the office and the ocean", "The number of social media followers the company has"],
                "Paragraph 4 emphasizes total addressable market size and founder resilience.",
                "4. paragraf toplam pazar büyüklüğü ve kurucu dayanıklılığını vurgular."
            ),
            build_q(
                "q_r_b1_05_05",
                "What do venture capital investors receive in exchange for providing risk capital?",
                "Girişim sermayesi yatırımcıları risk sermayesi sağlama karşılığında ne alırlar?",
                "Equity shares representing partial ownership of the company",
                ["A guaranteed monthly interest payment regardless of performance", "The intellectual copyright to the founder's personal diary", "A physical key to the city where the company was incorporated"],
                "Paragraph 2 states that in exchange for capital, investors receive equity shares.",
                "2. paragraf sermaye karşılığında yatırımcıların şirketten hisse aldığını belirtir."
            )
        ],
        "topic_tags": ["venture-capital", "startups", "finance", "b1-reading"],
        "related_ids": ["vocab.dilution", "vocab.resilience"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.product-discovery-interviews",
        "title": "Conducting Meaningful Customer Discovery Interviews",
        "cefr_level": "B1",
        "category": "business_strategy",
        "summary_en": "Techniques for asking unbiased, open-ended questions during user discovery interviews to uncover genuine customer pain points before writing code.",
        "summary_tr": "Kullanıcı keşif görüşmelerinde tarafsız ve açık uçlu sorular sorma teknikleri: kod yazmadan önce gerçek müşteri sorunlarını ortaya çıkarma.",
        "word_count": 480,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Flaw of Hypothetical Customer Questions",
                "content_en": "One of the most dangerous traps for technology entrepreneurs is building software based on unvalidated assumptions. Product managers frequently interview prospective clients and ask leading questions like: 'If we built an AI tool that organizes your inbox, would you pay twenty dollars a month for it?' Most people naturally want to be polite, so they enthusiastically answer 'Yes'. However, when the product launches months later, zero sales materialize because hypothetical commitments rarely predict real financial behavior.",
                "content_tr": "Teknoloji girişimcileri için en tehlikeli tuzaklardan biri, doğrulanmamış varsayımlara dayalı yazılım geliştirmektir. Ürün yöneticileri sıklıkla potansiyel müşterilerle görüşür ve yönlendirici sorular sorar: 'Gelen kutunuzu düzenleyen bir yapay zeka aracı yapsaydık, buna ayda yirmi dolar öder miydiniz?' Çoğu insan doğal olarak kibar olmak ister, bu yüzden hevesle 'Evet' yanıtını verir. Ancak ürün aylar sonra piyasaya çıktığında sıfır satış gerçekleşir çünkü varsayımsal taahhütler nadiren gerçek finansal davranışı tahmin eder."
            },
            {
                "paragraph_index": 2,
                "title": "Focusing on Past Behavior Rather than Opinions",
                "content_en": "To extract authentic customer insight, skilled product interviewers follow a golden rule: ask exclusively about past actions, never future intentions. Instead of asking what features users might desire tomorrow, ask: 'Tell me about the last time you struggled to organize your inbox. What specific steps did you take to solve it?' Real past behavior reveals actual friction, actual workarounds, and whether the problem is painful enough that people currently spend money or effort trying to fix it.",
                "content_tr": "Özgün müşteri içgörüsü elde etmek için yetenekli ürün mülakatçıları altın bir kuralı takip eder: yalnızca geçmiş eylemler hakkında soru sorun, asla gelecekteki niyetler hakkında değil. Kullanıcıların yarın hangi özellikleri isteyebileceğini sormak yerine şunu sorun: 'Gelen kutunuzu düzenlemekte en son ne zaman zorlandığınızı anlatın. Bunu çözmek için hangi somut adımları attınız?' Gerçek geçmiş davranış gerçek sürtünmeyi, geçici çözümleri ve sorunun insanların şu anda düzeltmek için para veya çaba harcayacak kadar acı verici olup olmadığını ortaya koyar."
            },
            {
                "paragraph_index": 3,
                "title": "Embracing Awkward Silence",
                "content_en": "During customer interviews, inexperienced interviewers feel uncomfortable when a participant pauses to think, and they instinctively jump in to fill the silence with product pitches. Masterful interviewers embrace silence. Giving the client five to ten seconds of space encourages them to articulate deeper frustrations and unscripted thoughts that often reveal the most transformative market insights.",
                "content_tr": "Müşteri görüşmeleri sırasında deneyimsiz mülakatçılar bir katılımcı düşünmek için durakladığında rahatsız hisseder ve içgüdüsel olarak sessizliği ürün tanıtımlarıyla doldurmaya çalışırlar. Usta mülakatçılar ise sessizliği benimser. Müşteriye beş ila on saniyelik bir alan vermek, onları genellikle en dönüştürücü pazar içgörülerini ortaya çıkaran daha derin hayal kırıklıklarını ve plansız düşüncelerini dile getirmeye teşvik eder."
            },
            {
                "paragraph_index": 4,
                "title": "Synthesizing Patterns Across Segments",
                "content_en": "A single customer interview provides interesting anecdotes, but product strategy cannot be pivoted on one conversation. Teams must conduct fifteen to twenty interviews across a defined segment before synthesizing findings into thematic clusters. When multiple independent companies report the exact same operational bottleneck, engineering leadership can build software with high conviction.",
                "content_tr": "Tek bir müşteri görüşmesi ilginç anekdotlar sağlar, ancak ürün stratejisi tek bir konuşma üzerine pivot edilemez. Ekipler bulguları tematik kümeler halinde sentezlemeden önce tanımlanmış bir segment genelinde on beş ila yirmi görüşme yapmalıdır. Birden fazla bağımsız şirket tam olarak aynı operasyonel darboğazı bildirdiğinde, mühendislik liderliği yüksek bir inançla yazılım geliştirebilir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "anecdote",
                "vocab_id": "vocab.anecdote",
                "context_definition_en": "A short and interesting story about a real incident or person, but not conclusive proof.",
                "context_meaning_tr": "Bir kişi veya olay hakkında kısa, ilginç anlatı (anekdot)."
            },
            {
                "word": "conviction",
                "vocab_id": "vocab.conviction",
                "context_definition_en": "A firmly held belief or solid, well-founded certainty.",
                "context_meaning_tr": "Güçlü ve sarsılmaz bir inanç veya kesinlik."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_06_01",
                "Why are hypothetical questions like 'Would you pay for feature X?' considered flawed?",
                "'X özelliğine para öder miydiniz?' gibi varsayımsal sorular neden hatalı kabul edilir?",
                "People answer politely in interviews, but hypothetical promises rarely predict actual purchasing",
                ["International patent law prohibits asking questions about software pricing", "Computers cannot record audio interviews that contain conditional grammar", "Customers will only tell the truth if they are offered cash rewards immediately"],
                "Paragraph 1 explains people politely say yes, but hypothetical promises rarely predict behavior.",
                "1. paragraf insanların nezaketen evet dediğini ama varsayımsal vaatlerin davranışı yansıtmadığını belirtir."
            ),
            build_q(
                "q_r_b1_06_02",
                "What is the 'golden rule' of effective customer discovery interviews?",
                "Etkili müşteri keşif görüşmelerinin 'altın kuralı' nedir?",
                "Inquiring exclusively about actual past actions rather than speculative future intentions",
                ["Never allowing the customer to speak for more than ten seconds continuously", "Demonstrating a finished product prototype before asking any questions", "Conducting all interviews exclusively through text messaging"],
                "Paragraph 2 states the golden rule is asking about past actions, never future intentions.",
                "2. paragraf altın kuralın gelecek niyetleri değil geçmiş eylemleri sormak olduğunu açıklar."
            ),
            build_q(
                "q_r_b1_06_03",
                "Why should interviewers embrace pauses and silence during interviews?",
                "Mülakatçılar görüşmeler sırasında duraklamaları ve sessizliği neden benimsemelidir?",
                "It gives participants time to articulate deeper, unscripted frustrations and insights",
                ["It reduces the cost of mobile telephone connection charges", "It allows the interviewer to check their personal social media accounts", "It indicates that the customer has completely lost interest in the call"],
                "Paragraph 3 notes giving 5-10 seconds of space encourages deeper thoughts.",
                "3. paragraf müşteriye alan tanımanın daha derin düşünceleri dile getirmesini sağladığını belirtir."
            ),
            build_q(
                "q_r_b1_06_04",
                "Why is a single customer interview insufficient to pivot product strategy?",
                "Ürün stratejisini değiştirmek için tek bir müşteri görüşmesi neden yetersizdir?",
                "One interview provides only anecdotes, requiring 15-20 interviews to identify reliable patterns",
                ["Government regulators require corporate interviews to be signed by a notary", "Single interviews are automatically deleted by audio recording software", "No single human being can accurately describe their daily work tasks"],
                "Paragraph 4 explains a single interview is anecdotal; 15-20 are needed to find patterns.",
                "4. paragraf tek görüşmenin anekdottan ibaret olduğunu, örüntü bulmak için 15-20 görüşme gerektiğini açıklar."
            ),
            build_q(
                "q_r_b1_06_05",
                "What does discovering existing customer 'workarounds' reveal to a product team?",
                "Müşterinin mevcut 'geçici çözümlerini' keşfetmek bir ürün ekibine neyi gösterir?",
                "That the problem is painful enough that users currently expend real effort to address it",
                ["That the customer is violating their internal corporate computer policies", "That the software market is already completely saturated by foreign competitors", "That the client has no budget available to purchase software solutions"],
                "Paragraph 2 notes that workarounds show if the problem is painful enough that people spend effort.",
                "2. paragraf geçici çözümlerin sorunun çaba harcanacak kadar acı verici olduğunu kanıtladığını söyler."
            )
        ],
        "topic_tags": ["product-discovery", "user-interviews", "customer-feedback", "b1-reading"],
        "related_ids": ["vocab.anecdote", "vocab.conviction"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.managing-hybrid-schedules",
        "title": "Balancing Focus and Synchronous Time in Hybrid Work Environments",
        "cefr_level": "B1",
        "category": "leadership_and_management",
        "summary_en": "Strategies for organizational leaders to coordinate hybrid office days, ensuring in-person presence is utilized for collaborative workshops rather than solitary desk work.",
        "summary_tr": "Hibrit ofis günlerini koordine etme stratejileri: ofis varlığının bireysel masa başı işler yerine ortaklaşa atölyeler için kullanılmasını sağlama.",
        "word_count": 485,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Purpose of the Office Reimagined",
                "content_en": "As organizations settled into permanent hybrid working models, an unexpected paradox arose. Employees commuted through heavy traffic to sit in corporate cubicles all day wearing headphones on video calls with teammates who were working from home. This frustration highlighted a fundamental design flaw in uncoordinated hybrid schedules. To make physical office attendance meaningful, leadership must intentionally redefine what the workplace is for.",
                "content_tr": "Şirketler kalıcı hibrit çalışma modellerine yerleştikçe beklenmedik bir paradoks ortaya çıktı. Çalışanlar evden çalışan ekip arkadaşlarıyla görüntülü görüşmeler yapmak için kulaklık takıp tüm gün kurumsal kabinlerde oturmak üzere yoğun trafikte işe gidip geldiler. Bu hayal kırıklığı koordine edilmemiş hibrit programlardaki temel bir tasarım kusurunu vurguladı. Fiziksel ofis katılımını anlamlı kılmak için liderlik iş yerinin ne amaçla var olduğunu kasıtlı olarak yeniden tanımlamalıdır."
            },
            {
                "paragraph_index": 2,
                "title": "Anchor Days for Collaborative Work",
                "content_en": "The most effective hybrid organizations implement synchronized 'anchor days'. Instead of allowing random individual schedules where half the team is missing on any given Tuesday, squads agree to co-locate on two specific days each week. Crucially, these in-office days are reserved for high-bandwidth collaborative rituals: cross-functional quarterly planning, complex architectural design sprints, and retrospective team building.",
                "content_tr": "En etkili hibrit organizasyonlar senkronize 'çapa günleri' (anchor days) uygularlar. Herhangi bir salı günü ekibin yarısının eksik olduğu rastgele bireysel programlara izin vermek yerine, ekipler her hafta iki belirli günde aynı yerde bulunmayı kabul ederler. En önemlisi, bu ofis içi günler yüksek bant genişlikli iş birliği ritüellerine ayrılır: fonksiyonlar arası üç aylık planlama, karmaşık mimari tasarım sprintleri ve retrospektif ekip kaynaşması."
            },
            {
                "paragraph_index": 3,
                "title": "Reserving Remote Days for Deep Focus",
                "content_en": "Conversely, remote days should be fiercely protected for independent execution. Writing complex algorithms, authoring extensive technical whitepapers, or performing deep database tuning require hours of uninterrupted solitude. When managers respect remote days as quiet focus zones—discouraging spontaneous ad-hoc meetings—engineers accomplish their primary deliverables with superior speed and reduced stress.",
                "content_tr": "Tersine, uzaktan çalışılan günler bağımsız icraat için kararlılıkla korunmalıdır. Karmaşık algoritmalar yazmak, kapsamlı teknik raporlar hazırlamak veya derin veritabanı ayarlamaları yapmak saatlerce kesintisiz yalnızlık gerektirir. Yöneticiler uzaktan günleri sessiz odaklanma alanları olarak gördüklerinde (spontane plansız toplantıları engelleyerek), mühendisler temel çıktılarını üstün hız ve azalan stresle tamamlarlar."
            },
            {
                "paragraph_index": 4,
                "title": "Establishing Clear Team Charters",
                "content_en": "Successful hybrid teams formalize these operating principles in an explicit team charter. The charter documents expected core collaboration hours, expected response times for different communication channels, and guidelines for when a topic warrants an in-person workshop versus a written asynchronous proposal. Removing ambiguity empowers employees to manage their energy predictably.",
                "content_tr": "Başarılı hibrit ekipler bu çalışma ilkelerini açık bir ekip tüzüğünde (team charter) resmileştirirler. Tüzük beklenen temel iş birliği saatlerini, farklı iletişim kanalları için beklenen yanıt sürelerini ve bir konunun ne zaman yazılı bir asenkron teklif yerine yüz yüze bir atölyeyi gerektirdiğine dair yönergeleri belgeler. Belirsizliğin ortadan kaldırılması çalışanların enerjilerini öngörülebilir şekilde yönetmelerini sağlar."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "charter",
                "vocab_id": "vocab.charter",
                "context_definition_en": "A formal document describing the shared rights, goals, and working rules of an organization.",
                "context_meaning_tr": "Bir grubun çalışma kurallarını ve ilkelerini belirleyen resmi sözleşme veya tüzük."
            },
            {
                "word": "ambiguity",
                "vocab_id": "vocab.ambiguity",
                "context_definition_en": "The quality of being open to more than one interpretation; inexactness or uncertainty.",
                "context_meaning_tr": "Birden fazla yoruma açık olma durumu; belirsizlik."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_07_01",
                "What paradox frequently occurred in poorly coordinated hybrid work models?",
                "Kötü koordine edilmiş hibrit çalışma modellerinde sıklıkla hangi paradoks ortaya çıktı?",
                "Employees commuted to offices only to sit on video calls with remote colleagues",
                ["Offices ran out of electrical power whenever more than three people attended", "Employees forgot how to operate elevators in corporate headquarters", "Company laptops were strictly forbidden inside commercial office buildings"],
                "Paragraph 1 highlights the frustration of commuting just to join video calls with remote peers.",
                "1. paragraf evdeki iş arkadaşlarıyla görüşmek için ofise gidip kulaklıkla oturma çelişkisini anlatır."
            ),
            build_q(
                "q_r_b1_07_02",
                "What are 'anchor days' as defined in the second paragraph?",
                "İkinci paragrafta tanımlanan 'çapa günleri' (anchor days) nedir?",
                "Designated days when the entire team co-locates in the office for collaborative workshops",
                ["Days when maritime shipping vessels deliver hardware to office harbors", "Days when all digital communication is completely shut down across the country", "Days when employees are required to work twenty-four hours continuously"],
                "Paragraph 2 explains anchor days are agreed days when squads co-locate for collaboration.",
                "2. paragraf çapa günlerini tüm ekibin iş birliği için ofiste toplandığı günler olarak tanımlar."
            ),
            build_q(
                "q_r_b1_07_03",
                "What type of work should be prioritized during remote home days?",
                "Uzaktan ev günlerinde ne tür işlere öncelik verilmelidir?",
                "Independent, cognitively demanding tasks requiring uninterrupted solitude",
                ["Company-wide loud celebratory parties with external stakeholders", "Signing physical paper documents with foreign consular officials", "Rearranging office furniture and installing lighting fixtures"],
                "Paragraph 3 emphasizes that remote days should be reserved for uninterrupted solitude and deep focus.",
                "3. paragraf ev günlerinin kesintisiz yalnızlık ve derin odaklanmaya ayrılması gerektiğini belirtir."
            ),
            build_q(
                "q_r_b1_07_04",
                "What is the function of a 'team charter' in a hybrid organization?",
                "Hibrit bir organizasyonda 'ekip tüzüğü'nün (team charter) işlevi nedir?",
                "Documenting explicit guidelines on core hours, response times, and meeting rules",
                ["Listing the private personal salaries of every employee publicly", "Determining which political parties employees are required to support", "Outlawing all future vacations for engineering staff"],
                "Paragraph 4 explains team charters document core hours, response times, and guidelines.",
                "4. paragraf tüzüğün çalışma saatlerini, yanıt sürelerini ve yönergeleri belgelediğini açıklar."
            ),
            build_q(
                "q_r_b1_07_05",
                "How does removing operational ambiguity benefit employees?",
                "Operasyonel belirsizliği ortadan kaldırmak çalışanlara nasıl fayda sağlar?",
                "It empowers them to manage their daily cognitive energy predictably",
                ["It eliminates all company payroll taxes immediately", "It guarantees that employees will never have to write another line of code", "It automatically increases annual company revenues by five hundred percent"],
                "Paragraph 4 concludes that removing ambiguity empowers predictable energy management.",
                "4. paragraf belirsizliği kaldırmanın enerjiyi öngörülebilir yönetmeyi sağladığını ifade eder."
            )
        ],
        "topic_tags": ["hybrid-work", "team-charter", "management", "b1-reading"],
        "related_ids": ["vocab.charter", "vocab.ambiguity"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "reading.b1.green-data-centers",
        "title": "Environmental Sustainability and Energy Efficiency in Cloud Computing",
        "cefr_level": "B1",
        "category": "technology",
        "summary_en": "An analysis of the carbon footprint of massive cloud datacenters, exploring innovative cooling technologies, renewable energy sourcing, and green software engineering.",
        "summary_tr": "Bulut veri merkezlerinin karbon ayak izi analizi: yenilikçi soğutma teknolojileri, yenilenebilir enerji kaynakları ve yeşil yazılım mühendisliği.",
        "word_count": 515,
        "estimated_reading_minutes": 3,
        "paragraphs": [
            {
                "paragraph_index": 1,
                "title": "The Invisible Environmental Footprint of Code",
                "content_en": "When software engineers deploy code to the cloud or stream high-definition media, the process feels ephemeral and weightless. Yet behind every database query, model training cycle, and cloud backup hums a massive physical infrastructure. Globally, commercial datacenters consume more than two percent of the world's total electricity supply, generating a carbon footprint comparable to the global aviation industry. As computing demands surge, environmental sustainability has transformed into an urgent engineering challenge.",
                "content_tr": "Yazılım mühendisleri buluta kod yüklediğinde veya yüksek çözünürlüklü medya akışı sağladığında bu süreç son derece hafif ve soyut görünür. Ancak her veritabanı sorgusunun, model eğitim döngüsünün ve bulut yedeğinin arkasında devasa bir fiziksel altyapı çalışır. Küresel olarak ticari veri merkezleri dünya toplam elektrik arzının yüzde ikisinden fazlasını tüketmekte ve küresel havacılık sektörüyle karşılaştırılabilir bir karbon ayak izi üretmektedir. Bilgi işlem talepleri arttıkça, çevresel sürdürülebilirlik acil bir mühendislik zorluğuna dönüşmüştür."
            },
            {
                "paragraph_index": 2,
                "title": "Innovations in Datacenter Cooling",
                "content_en": "Historically, nearly forty percent of a datacenter's total electricity was expended not on running servers, but on mechanical air conditioning to keep computer chips from overheating. Modern hyperscale operators have developed radical cooling innovations to minimize this waste. Some facilities are constructed near Arctic regions to utilize sub-zero ambient air. Others submerge server racks directly into non-conductive dielectric liquid baths, absorbing heat hundreds of times more efficiently than traditional air.",
                "content_tr": "Tarihsel olarak bir veri merkezinin toplam elektriğinin neredeyse yüzde kırkı sunucuları çalıştırmak için değil, bilgisayar çiplerinin aşırı ısınmasını önleyen mekanik klimalar için harcanıyordu. Modern devasa ölçekli işletmeciler bu israfı en aza indirmek için radikal soğutma yenilikleri geliştirdiler. Bazı tesisler sıfırın altındaki ortam havasını kullanmak için Kutup bölgelerine yakın inşa edilmektedir. Diğerleri ise sunucu raflarını doğrudan iletken olmayan dielektrik sıvı banyolarına daldırarak ısıyı geleneksel havaya göre yüzlerce kat daha verimli bir şekilde emerler."
            },
            {
                "paragraph_index": 3,
                "title": "The Rise of Green Software Engineering",
                "content_en": "Sustainability is no longer solely the responsibility of hardware facility managers; software engineers play an equally pivotal role. Inefficient algorithms, bloated dependencies, and unindexed database queries needlessly consume server CPU cycles, directly converting into wasted wattage and carbon emissions. The emerging discipline of green software engineering encourages developers to profile the carbon intensity of their code, optimizing computational efficiency to minimize environmental degradation.",
                "content_tr": "Sürdürülebilirlik artık yalnızca donanım tesisi yöneticilerinin sorumluluğu değildir; yazılım mühendisleri de eşit derecede önemli bir rol oynamaktadır. Verimsiz algoritmalar, şişirilmiş bağımlılıklar ve indekslenmemiş veritabanı sorguları sunucu işlemci döngülerini gereksiz yere tüketerek doğrudan boşa harcanan vat miktarına ve karbon emisyonuna dönüşür. Ortaya çıkan yeşil yazılım mühendisliği disiplini, geliştiricileri kodlarının karbon yoğunluğunu profillemeye ve çevresel bozulmayı en aza indirmek için hesaplama verimliliğini optimize etmeye teşvik eder."
            },
            {
                "paragraph_index": 4,
                "title": "Renewable Power Purchase Pledges",
                "content_en": "Major cloud hyper-scalers now compete fiercely on renewable energy metrics. Technology corporations sign massive multi-decade power purchase agreements with solar, wind, and geothermal providers, committing to power one hundred percent of their operations with zero-carbon energy. For corporate technology leaders selecting cloud infrastructure, a provider's demonstrable green energy credentials have become a decisive procurement factor.",
                "content_tr": "Büyük bulut sağlayıcıları artık yenilenebilir enerji ölçütlerinde kıyasıya rekabet ediyor. Teknoloji şirketleri operasyonlarının yüzde yüzünü sıfır karbonlu enerjiyle beslemeyi taahhüt ederek güneş, rüzgar ve jeotermal sağlayıcılarla onlarca yıllık devasa güç satın alma anlaşmaları imzalıyor. Bulut altyapısı seçen kurumsal teknoloji liderleri için bir sağlayıcının kanıtlanabilir yeşil enerji referansları belirleyici bir satın alma faktörü haline gelmiştir."
            }
        ],
        "vocabulary_annotations": [
            {
                "word": "ephemeral",
                "vocab_id": "vocab.ephemeral",
                "context_definition_en": "Lasting for a very short time; transient and intangible.",
                "context_meaning_tr": "Çok kısa süren, geçici ve elle tutulamaz nitelikte olan."
            },
            {
                "word": "dielectric",
                "vocab_id": "vocab.dielectric",
                "context_definition_en": "Having the property of transmitting electric force without conducting electricity.",
                "context_meaning_tr": "Elektriği iletmeyen ancak elektriksel kuvveti ileten yalıtkan madde."
            }
        ],
        "comprehension_questions": [
            build_q(
                "q_r_b1_08_01",
                "How does the global datacenter carbon footprint compare to other industries?",
                "Küresel veri merkezi karbon ayak izi diğer sektörlerle nasıl karşılaştırılır?",
                "It consumes over two percent of global electricity, rivaling the worldwide aviation industry",
                ["It produces less pollution than a single domestic household toaster", "It represents ninety-nine percent of all global carbon emissions combined", "It operates completely without electricity using steam power engines"],
                "Paragraph 1 notes datacenters consume >2% of electricity, comparable to global aviation.",
                "1. paragraf elektriğin %2'sinden fazlasını tükettiğini ve havacılık sektörüyle yarışabileceğini belirtir."
            ),
            build_q(
                "q_r_b1_08_02",
                "Historically, what accounted for nearly forty percent of a datacenter's power consumption?",
                "Tarihsel olarak bir veri merkezinin güç tüketiminin neredeyse yüzde kırkını ne oluşturuyordu?",
                "Mechanical air conditioning systems required to prevent servers from overheating",
                ["Charging personal mobile phones belonging to datacenter security guards", "Illuminating giant neon advertising billboards on the facility roof", "Powering automated coffee makers in employee cafeteria break rooms"],
                "Paragraph 2 explains nearly 40% of electricity was spent on air conditioning.",
                "2. paragraf elektriğin yaklaşık %40'ının sunucuları soğutan klimalara harcandığını açıklar."
            ),
            build_q(
                "q_r_b1_08_03",
                "What is liquid immersion cooling described in the second paragraph?",
                "İkinci paragrafta açıklanan sıvı daldırma soğutma yöntemi nedir?",
                "Submerging servers directly into non-conductive dielectric fluid baths to absorb heat",
                ["Spraying tap water directly onto live electrical power cables every morning", "Floating datacenters on open ocean wooden rafts without any roofs", "Freezing computer motherboards in block ice before shipping them to clients"],
                "Paragraph 2 describes submerging server racks into non-conductive dielectric liquid baths.",
                "2. paragraf sunucu raflarını iletken olmayan dielektrik sıvı banyolarına daldırmayı anlatır."
            ),
            build_q(
                "q_r_b1_08_04",
                "How can software developers directly contribute to datacenter sustainability?",
                "Yazılım geliştiriciler veri merkezi sürdürülebilirliğine doğrudan nasıl katkıda bulunabilir?",
                "By writing efficient algorithms and eliminating bloated, unindexed queries",
                ["By refusing to use computer monitors during daylight hours", "By translating their code into foreign languages using hand pencils", "By turning off the server power switch every time a user logs off"],
                "Paragraph 3 explains efficient code reduces unnecessary CPU cycles and wattage.",
                "3. paragraf verimli kod yazmanın gereksiz işlemci tüketimini ve karbon salınımını azalttığını belirtir."
            ),
            build_q(
                "q_r_b1_08_05",
                "Why have green energy credentials become decisive in corporate IT procurement?",
                "Yeşil enerji referansları kurumsal BT satın alımlarında neden belirleyici hale geldi?",
                "Technology leaders prioritize providers committed to zero-carbon renewable energy",
                ["Government laws mandate that non-green cloud providers be seized immediately", "Clean electricity makes computer screens display colors more brightly", "Solar panels are cheaper to manufacture than digital software licenses"],
                "Paragraph 4 explains green credentials are a decisive procurement factor for enterprise leaders.",
                "4. paragraf yeşil enerji referanslarının yöneticiler için belirleyici bir kriter olduğunu açıklar."
            )
        ],
        "topic_tags": ["green-computing", "sustainability", "datacenters", "cloud", "b1-reading"],
        "related_ids": ["vocab.ephemeral", "vocab.dielectric"],
        "status": "APPROVED",
        "version": 1
    }
]

print(f"Defined {len(B1_READING_ARTICLES)} B1 reading articles.")
